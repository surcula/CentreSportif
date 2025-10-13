package servlets;

import Tools.ParamUtils;
import Tools.Result;
import business.ServletUtils;
import business.SportBusiness;
import controllers.helpers.HallControllerHelper;
import controllers.helpers.SportControllerHelper;
import dto.EMF;
import dto.Page;
import dto.SportUpdateForm;
import entities.Sport;
import enums.Scope;
import mappers.SportMapper;
import services.SportServiceImpl;

import javax.persistence.EntityManager;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

import static constants.Rooting.*;
import static constants.Rooting.TEMPLATE;

@WebServlet(name = "SportServlet", value = "/sport")
public class SportServlet extends HttpServlet {


    // Log4j
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SportServlet.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        HttpSession session = request.getSession(false);
        String form = request.getParameter("form");
        String editForm = request.getParameter("editForm");
        String role = (session != null && session.getAttribute("role") != null)
                ? session.getAttribute("role").toString()
                : null;
        boolean fullAccess = ServletUtils.isFullAuthorized(role);

        // --- Formulaire de création ---
        if (form != null) {
            if (!fullAccess) {
                ServletUtils.redirectNoAuthorized(request, response);
                return;
            }

            log.info("Form: " + form);

            SportControllerHelper.handleFormDisplay(request, response);
            return;
            // --- Formulaire d'édition ---
        } else if (editForm != null) {
            log.info("editForm: " + editForm);
            if (!fullAccess) {
                ServletUtils.redirectNoAuthorized(request, response);
                return;
            }
            Result<Integer> sportUpdateId = ParamUtils.verifyId(editForm);
            if (!sportUpdateId.isSuccess()) {
                log.error("Erreur lors de la conversion de l'id du sport");
                ServletUtils.forwardWithError(request, response, "Erreur lors de la conversion de l'id du sport", SPORT_JSP, TEMPLATE);
                return;
            }
            EntityManager em = EMF.getEM();
            try {
                SportServiceImpl sportService = new SportServiceImpl(em);
                Result<Sport> result = sportService.getOneById(sportUpdateId.getData());
                if (result.isSuccess()) {
                    SportControllerHelper.handleEditForm(request, response, result.getData());
                } else {
                    ServletUtils.forwardWithErrors(request, response, result.getErrors(), SPORT_FORM_JSP, TEMPLATE);
                    return;
                }
            } catch (Exception e) {
                log.error("Erreur lors du chargement du sport en édition", e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), SPORT_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null) em.close();
            }
        } else {
            EntityManager em = EMF.getEM();
            try {
                SportServiceImpl sportService = new SportServiceImpl(em);
                SportBusiness sportBusiness = new SportBusiness(sportService);

                Result<Integer> pageRes = ParamUtils.stringToInteger(request.getParameter("page"));
                int page = pageRes.isSuccess() ? Math.max(1, pageRes.getData()) : 1;

                Result<Integer> sizeRes = ParamUtils.stringToInteger(request.getParameter("size"));
                int size = sizeRes.isSuccess() ? Math.min(20, Math.max(1, sizeRes.getData())) : 10;

                Result<Page<Sport>> result = fullAccess
                        ? sportBusiness.getAllSportsPaged(page, size, Scope.ALL)
                        : sportBusiness.getAllSportsPaged(page, size, Scope.ACTIVE);

                if (result.isSuccess()) {
                    Page<Sport> p = result.getData();
                    request.setAttribute("sports", p.getContent());
                    request.setAttribute("page", p.getPage());
                    request.setAttribute("size", p.getSize());
                    request.setAttribute("totalPages", p.getTotalPages());
                    request.setAttribute("totalElements", p.getTotalElements());
                    request.setAttribute("fullAccess", fullAccess);

                    SportControllerHelper.handleList(request, response, p.getContent());
                } else {
                    ServletUtils.forwardWithErrors(request, response, result.getErrors(), SPORT_JSP, TEMPLATE);
                    return;
                }
            } catch (Exception e) {
                log.error("Erreur doGet sport", e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), SPORT_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null) em.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Vérifier les droits
        HttpSession session = request.getSession(false);
        if (!(session != null && ServletUtils.isFullAuthorized(session.getAttribute("role").toString()))) {
            ServletUtils.redirectNoAuthorized(request, response);
            return;
        }
        String action = request.getParameter("action");
        String idParam = request.getParameter("sportId");

        //On vérifie l'id si c'est possible ou non.
        Result<Integer> idResult = ParamUtils.verifyId(idParam);
        if (idParam != null) {

            if (!idResult.isSuccess()) {
                ServletUtils.forwardWithErrors(request, response, idResult.getErrors(), SPORT_JSP, TEMPLATE);
                return;
            }
        }

        //Activer ou softDelete un sport
        if ("activer".equals(action) || "delete".equals(action)) {
            EntityManager em = EMF.getEM();
            try {
                SportServiceImpl sportService = new SportServiceImpl(em);
                em.getTransaction().begin();
                Result<Sport> sportResult = sportService.getOneById(idResult.getData());
                if (!sportResult.isSuccess()) {
                    log.error("Erreur sport non trouvé ");
                    ServletUtils.forwardWithError(request, response, "erreur sport non trouvé.", SPORT_JSP, TEMPLATE);
                    return;
                }
                sportResult.getData().setActive(ServletUtils.changeActive(sportResult.getData().isActive()));
                sportService.update(sportResult.getData());
                em.getTransaction().commit();
                log.info("sport " + sportResult.getData().getId() + " activé/softDelete avec succès");
                ServletUtils.redirectWithMessage(request, response, "sport activé/softDelete avec succès", "success", "/sport");
                return;
            } catch (Exception e) {
                log.error("Erreur d'activation/softDelete : " + e.getMessage());
                ServletUtils.forwardWithError(request, response, e.getMessage(), SPORT_JSP, TEMPLATE);
            } finally {
                if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
                if (em != null) em.close();
            }
            return;
        } else {
            //Create
            //Verification des champs
            if (idParam == null) {
                Result<Sport> sportCreateResult = SportBusiness.initCreateForm(
                        request.getParameter("sportName"),
                        request.getParameter("active")
                );
                if (!sportCreateResult.isSuccess()) {
                    log.error("Erreur lors de la " + (idParam == null ? "création" : "édition") + "du sport : " + sportCreateResult.getErrors());
                    ServletUtils.forwardWithErrors(request, response, sportCreateResult.getErrors(), SPORT_FORM_JSP, TEMPLATE);
                    return;
                }
                EntityManager em = EMF.getEM();
                try {
                    SportServiceImpl sportService = new SportServiceImpl(em);
                    em.getTransaction().begin();
                    sportService.create(sportCreateResult.getData());
                    em.getTransaction().commit();
                    log.info("sport créé avec success.");
                    ServletUtils.redirectWithMessage(request, response, "sport avec success.", "success", "/sport");
                    return;
                } catch (Exception e) {
                    log.error("Une erreur est survenue lors de la création du sport. " + e.getMessage());
                    ServletUtils.forwardWithError(request, response, "Une erreur est survenue lors de la création du sport. ", SPORT_FORM_JSP, TEMPLATE);
                    return;
                } finally {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    if (em != null) em.close();
                }

                //Edit
            } else {
                Result<SportUpdateForm> sportUpdateFormResult = SportBusiness.initUpdateForm(
                        request.getParameter("sportName"),
                        request.getParameter("active")
                );
                if (!sportUpdateFormResult.isSuccess()) {
                    log.error("Erreur lors de la " + (idParam == null ? "création" : "édition") + "du sport : " + sportUpdateFormResult.getErrors());
                    ServletUtils.forwardWithErrors(request, response, sportUpdateFormResult.getErrors(), SPORT_FORM_JSP, TEMPLATE);
                    return;
                }
                EntityManager em = EMF.getEM();
                try {
                    SportServiceImpl sportService = new SportServiceImpl(em);
                    em.getTransaction().begin();
                    Result<Sport> sport = sportService.getOneById(idResult.getData());
                    if (!sport.isSuccess()) {
                        log.error("sport :" + idResult.getData() + " n'existe pas");
                        ServletUtils.forwardWithError(request, response, "Le sport n'existe pas", SPORT_FORM_JSP, TEMPLATE);
                        return;
                    }

                    SportMapper.fromUpdateForm(sportUpdateFormResult.getData(), sport.getData());
                    sportService.update(sport.getData());
                    em.getTransaction().commit();
                    log.info("sport modifié avec success.");
                    ServletUtils.redirectWithMessage(request, response, "sport modifié avec success.", "success", "/sport");
                    return;
                } catch (Exception e) {
                    log.error("Erreur lors de l'update du sport", e);
                    ServletUtils.forwardWithError(request, response, "Erreur lors de l'update du sport", SPORT_FORM_JSP, TEMPLATE);
                    return;
                } finally {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    if (em != null) em.close();
                }
            }
        }

    }
}
 