package servlets;


import Tools.ParamUtils;
import business.EventBusiness;
import controllers.helpers.EventControllerHelper;
import dto.Page;
import entities.Event;
import enums.Scope;
import javafx.beans.binding.IntegerExpression;
import services.EventServiceImpl;
import business.ServletUtils;
import static constants.Rooting.*;
import javax.persistence.EntityManager;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import dto.EMF;
import Tools.Result;



/**
 * The class to view the JSP form page and to send the data of the form in DB
 * @author Franz
 *
 */
@MultipartConfig
@WebServlet(name = "Event", value = "/event")
public class EventServlet extends HttpServlet {
    // Log4j
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EventServlet.class);

    /**
     * Initialisation of the servlet with a message to be sur ethe servlet is activated
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log.info(">>> [EventServlet] Initialisation de la servlet");
    }
    /**
     * Variable for the entity manager
     */
    private EntityManager em;
    /**
     * variable to create the service for event
     */
    private EventServiceImpl eventService;
    /**
     * Method to view the form and the list of events
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); //Retourne une session si existante
        String form = request.getParameter("form");
        String editForm = request.getParameter("editForm");
        String role = (session != null && session.getAttribute("role") != null)
              ? session.getAttribute("role").toString()
                : null;
        boolean fullAccess = ServletUtils.isFullAuthorized(role);
        //--- Formulaire de création ---
        if (form != null) {
            if (!fullAccess) {
                ServletUtils.redirectNoAuthorized(request, response);
                return;
            }

            EventControllerHelper.handleFormDisplay(request, response);
            return;
        }

        //--- Formulaire d'édition ---
        if (editForm != null) {
            if (!fullAccess) {
                ServletUtils.redirectNoAuthorized(request, response);
                return;
            }
            EntityManager em = EMF.getEM();
            try {
                EventServiceImpl eventService = new EventServiceImpl(em);
                Result<Event> result = eventService.getOneById(Integer.parseInt(editForm));
                if (result.isSuccess()) {
                    EventControllerHelper.handleEditForm(request, response, result.getData());
                } else {
                    ServletUtils.forwardWithErrors(request, response, result.getErrors(), EVENT_FORM_JSP, TEMPLATE);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                ServletUtils.forwardWithError(request, response, e.getMessage(), EVENT_JSP, TEMPLATE);
            } finally {
                em.close();
            }
            return;
        }
        // --- Liste paginée ---
        EntityManager em = EMF.getEM();
        try {
            EventServiceImpl eventService = new EventServiceImpl(em);
            EventBusiness eventBusiness = new EventBusiness(eventService, em);

            Result<Integer> pageRes = ParamUtils.stringToInteger(request.getParameter("page"));
            int page = pageRes.isSuccess() ? Math.max(1, pageRes.getData()) : 1;

            Result<Integer> sizeRes = ParamUtils.stringToInteger(request.getParameter("size"));
            int size = sizeRes.isSuccess() ? Math.min(10, Math.max(1, sizeRes.getData())) : 10;

            Result<Page<Event>> result = fullAccess
                    ? eventBusiness.getAllEventsPaged(page, size, Scope.ALL)
                    : eventBusiness.getAllEventsPaged(page, size, Scope.ACTIVE);

            if (result.isSuccess()) {
                Page<Event> p = result.getData();
                request.setAttribute("page", p.getPage());
                request.setAttribute("size", p.getSize());
                request.setAttribute("totalPages", p.getTotalPages());
                request.setAttribute("totalElements", p.getTotalElements());
                request.setAttribute("fullAccess", fullAccess);
                EventControllerHelper.handleList(request, response, p.getContent());
                return;

            } else {
                ServletUtils.forwardWithErrors(request, response, result.getErrors(), EVENT_JSP, TEMPLATE);
            }
        } catch (Exception e) {
            log.error("Erreur doGet events", e); // <= avec la stack trace
            ServletUtils.forwardWithError(request, response, e.getMessage(), EVENT_JSP, TEMPLATE);
        } finally {
            em.close();
        }
    }
    /**
     * The method to send data in DB
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @return request
     * @return response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        em = EMF.getEM();
        eventService = new EventServiceImpl(em);
        String eventIdStr = request.getParameter("eventId");
        boolean isEdit = (eventIdStr != null && !eventIdStr.isEmpty());
        log.info("doPost invoked - eventIdStr = " + request.getParameter("eventId"));
        //Changer le statut de Terminé vers En cours et supprimer mettre le statut de En cours vers terminé
        String action = request.getParameter("action");
        if (isEdit && action != null && !action.isEmpty()) {
            try {
                int eventId = Integer.parseInt(eventIdStr);
                Result<Event> existingEventRest = eventService.getOneById(eventId);

                if(!existingEventRest.isSuccess()) {
                    ServletUtils.redirectWithMessage(request, response, "Evènement introuvable pour la mise à jour du statut.", EVENT_JSP, TEMPLATE);
                    return;
                }
                Event eventToUpdate = existingEventRest.getData();

                em.getTransaction().begin();;

                String successMessage = "";
                if ("delete".equals(action)) {
                    //utilisation de delete du EventServiceImpl
                    eventService.delete(eventToUpdate);
                    successMessage = "Evènènement marqué comme terminé avec succès.";
                } else if ("activer".equals(action)) {
                    //utilisation de update du EventServiceImpl
                    eventToUpdate.setActive(true);;
                    eventService.update(eventToUpdate);
                    successMessage = "Evènement remis En cours avec succès.";
                } else {
                    //opération inadéquate
                    em.getTransaction().rollback();
                    ServletUtils.redirectWithMessage(request, response, "Action de statut non reconnue.", EVENT_JSP, TEMPLATE );
                    return;
                }
                em.getTransaction().commit();

                ServletUtils.redirectWithMessage(request, response, successMessage, "success", "/event");
                return;

            }catch (Exception e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                ServletUtils.redirectWithMessage(request, response, "Erreur lors de la mise à jour du statut : " +  e.getMessage(), EVENT_JSP, TEMPLATE);
                return;
            } finally {
                if(em != null) em.close();
            }
        }

        // initialisation de l'entité Event
        Event event;
        String currentPicture = null;


        try {
            if (isEdit) {
                //MODIFICATION
                int eventId = Integer.parseInt(eventIdStr);
                Result<Event> existingEventRes = eventService.getOneById(eventId);
                if(!existingEventRes.isSuccess()) {
                    ServletUtils.forwardWithError(request, response, "Evènement introuvable.", EVENT_FORM_JSP, TEMPLATE);
                    return;
                }
                event = existingEventRes.getData();
                currentPicture = event.getPicture();
            }
            else {
                //CREATION
                event = new Event();
            }
            //traitement de récupération de l'image
            Part imagePart = request.getPart("image");
            String imageName = null;
            if(imagePart != null && imagePart.getSize() > 0) {
                //Récupération du nom de l'image
                String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
                //Création du nom unique pour l'image
                String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
                //Configuration du dossier de destination de l'image
                String uploadDirectory = getServletContext().getRealPath("/webapp/assets/img/uploads");
                //Création du dossier s'il n'existe pas
                Files.createDirectories(Paths.get(uploadDirectory));
                //Chemin complet pour l'image
                Path filePath = Paths.get(uploadDirectory, uniqueFileName);
                //sauvegarde du fichier
                imagePart.write(filePath.toAbsolutePath().toString());
                imageName = uniqueFileName;
            }
            //Validation des données
            Result<Event> baseresult = EventBusiness.initCreateForm(
                    request.getParameter("eventName"),
                    request.getParameter("startDateHour"),
                    request.getParameter("endDateHour"),
                    request.getParameter("description"),
                    imageName,
                    request.getParameter("status")
            );
            if(baseresult.isSuccess()) {
                Event filledEvent = baseresult.getData();

                em.getTransaction().begin();
                if(isEdit) {
                    event.setEventName(filledEvent.getEventName());
                    event.setBeginDateHour(filledEvent.getBeginDateHour());
                    event.setEndDateHour(filledEvent.getEndDateHour());
                    event.setInfo(filledEvent.getInfo());
                    //pour ne pas devoir changer l'image et pouvoir valider le formulaire sans devoir insérer une nouvelle image
                    if(filledEvent.getPicture() !=null && !filledEvent.getPicture().isEmpty()){
                        event.setPicture(filledEvent.getPicture());
                    }
                    event.setPicture(filledEvent.getPicture());
                    event.setActive(filledEvent.isActive());
                    eventService.update(event);
                } else {
                    eventService.create(filledEvent);
                }
                em.getTransaction().commit();

                ServletUtils.redirectWithMessage(request, response, isEdit ? "Evènement modifié avec succès" : "Evènement créé avec succès",
                        "success", "/event");
            } else {
                request.setAttribute("event", isEdit ? event : baseresult.getData());
                ServletUtils.forwardWithErrors(request, response, baseresult.getErrors(), EVENT_FORM_JSP, TEMPLATE);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ServletUtils.forwardWithError(request, response, e.getMessage(), EVENT_FORM_JSP, TEMPLATE);
        } finally {
            if (em != null) em.close();
        }
    }
}