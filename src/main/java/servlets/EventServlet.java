package servlets;


import business.EventBusiness;
import controllers.helpers.EventControllerHelper;
import dto.Page;
import entities.Event;
import enums.Scope;
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
     * variable to create the business for event
     */
    private EventBusiness eventBusiness;
    //private EntityManagerFactory entityManagerFactory;
    /**
     * Method to view the form
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
            EventBusiness eventBusiness = new EventBusiness(eventService);

            Result<Integer> pageRes = ServletUtils.stringToInteger(request.getParameter("page"));
            int page = pageRes.isSuccess() ? Math.max(1, pageRes.getData()) : 1;

            Result<Integer> sizeRes = ServletUtils.stringToInteger(request.getParameter("size"));
            int size = sizeRes.isSuccess() ? Math.min(10, Math.max(1, sizeRes.getData())) : 10;

            Result<Page<Event>> result = fullAccess
                    ? eventBusiness.getAllEventsPaged(page, size, Scope.ALL)
                    : eventBusiness.getAllEventsPaged(page, size, Scope.ACTIVE);

            if (result.isSuccess()) {
                Page<Event> p = result.getData();
                //request.setAttribute("events", p.getContent());
                request.setAttribute("page", p.getPage());
                request.setAttribute("size", p.getSize());
                request.setAttribute("totalPages", p.getTotalPages());
                request.setAttribute("totalElements", p.getTotalElements());
                request.setAttribute("fullAccess", fullAccess);
                EventControllerHelper.handleList(request, response, p.getContent());
                //request.getRequestDispatcher("/views/template/template.jsp").forward(request, response);
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

        //vérifier les droits lorsque l'utilisateur est connecté mais la connexion n'est pas opérationnelle
        //HttpSession session = request.getSession(false);
        //if(session == null || ServletUtils.isFullAuthorized(session.getAttribute("role").toString())) {
        //    ServletUtils.redirectWithMessage(request, response, "Accès refusé", "error", "/home");
        //    return;
        //}
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

        if(baseresult.isSuccess()){
            try {
                em = EMF.getEM();
                eventService = new EventServiceImpl(em);
                em.getTransaction().begin();

                eventService.create(baseresult.getData());
                em.getTransaction().commit();
                ServletUtils.redirectWithMessage(request, response, "Evènement créé avec succès", "success", "/views/template/template.jsp?content=../event.jsp" );
            }catch (Exception e) {
                if(em.getTransaction().isActive()) em.getTransaction().rollback();
                ServletUtils.forwardWithError(request, response, e.getMessage(), EVENT_FORM_JSP, TEMPLATE);
            }finally {
                if (em != null) em.close();
            }
        }else {
            ServletUtils.forwardWithErrors(request, response, baseresult.getErrors(), EVENT_FORM_JSP,TEMPLATE);
        }
    }
}