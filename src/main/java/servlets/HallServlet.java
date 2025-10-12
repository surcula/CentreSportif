package servlets;

import Tools.ParamUtils;
import Tools.Result;
import business.HallBusiness;
import business.ServletUtils;
import controllers.helpers.HallControllerHelper;
import dto.EMF;
import dto.HallUpdateForm;
import dto.Page;
import entities.Hall;
import enums.Scope;
import mappers.HallMapper;
import services.HallServiceImpl;
import static constants.Rooting.*;
import javax.persistence.EntityManager;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "Hall", value = "/hall")
public class HallServlet extends HttpServlet {


    // Log4j
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HallServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false); //Retourne une session si existante
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
            HallControllerHelper.handleFormDisplay(request, response);
            return;
            // --- Formulaire d'édition ---
        } else if (editForm != null) {
            if (!fullAccess) {
                ServletUtils.redirectNoAuthorized(request, response);
                return;
            }
            Result<Integer> hallUpdateId = ParamUtils.verifyId(editForm);
            if (!hallUpdateId.isSuccess()) {
                log.error("Erreur lors de la conversion de l'id du hall" );
                ServletUtils.forwardWithError(request, response, "Erreur lors de la conversion de l'id du hall", HALL_JSP, TEMPLATE);
                return;
            }
            EntityManager em = EMF.getEM();
            try {
                HallServiceImpl hallService = new HallServiceImpl(em);
                Result<Hall> result = hallService.getOneById(hallUpdateId.getData());

                if (result.isSuccess()) {
                    HallControllerHelper.handleEditForm(request, response, result.getData());
                    return;
                } else {
                    ServletUtils.forwardWithErrors(request, response, result.getErrors(), HALL_FORM_JSP, TEMPLATE);
                    return;
                }
            } catch (Exception e) {
                log.error("Erreur lors du chargement du formulaire du hall", e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), HALL_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null) em.close();
            }
        } else {
            // --- Liste paginée ---
            EntityManager em = EMF.getEM();
            try {
                HallServiceImpl hallService = new HallServiceImpl(em);
                HallBusiness hallBusiness = new HallBusiness(hallService);

                Result<Integer> pageRes = ParamUtils.stringToInteger(request.getParameter("page"));
                int page = pageRes.isSuccess() ? Math.max(1, pageRes.getData()) : 1;

                Result<Integer> sizeRes = ParamUtils.stringToInteger(request.getParameter("size"));
                int size = sizeRes.isSuccess() ? Math.min(20, Math.max(1, sizeRes.getData())) : 10;

                Result<Page<Hall>> result = fullAccess
                        ? hallBusiness.getAllHallsPaged(page, size, Scope.ALL)
                        : hallBusiness.getAllHallsPaged(page, size, Scope.ACTIVE);

                if (result.isSuccess()) {
                    Page<Hall> p = result.getData();
                    request.setAttribute("halls", p.getContent());
                    request.setAttribute("page", p.getPage());
                    request.setAttribute("size", p.getSize());
                    request.setAttribute("totalPages", p.getTotalPages());
                    request.setAttribute("totalElements", p.getTotalElements());
                    request.setAttribute("fullAccess", fullAccess);
                    HallControllerHelper.handleList(request, response, p.getContent());

                } else {
                    ServletUtils.forwardWithErrors(request, response, result.getErrors(), HALL_JSP, TEMPLATE);
                    return;
                }
            } catch (Exception e) {
                log.error("Erreur doGet halls", e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), HALL_JSP, TEMPLATE);
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
        String role = (session != null) ? (String) session.getAttribute("role") : null;
        if (role == null || !ServletUtils.isFullAuthorized(role)) {
            ServletUtils.redirectNoAuthorized(request, response);
            return;
        }

        String action = request.getParameter("action");
        String idParam = request.getParameter("hallId");

        //On vérifie l'id si c'est possible ou non.
        Result<Integer> idResult = ParamUtils.verifyId(idParam);
        if (idParam != null) {

            if (!idResult.isSuccess()) {
                ServletUtils.forwardWithErrors(request, response, idResult.getErrors(), HALL_JSP, TEMPLATE);
                return;
            }
        }

        //Activer ou softDelete un hall
        if ("activer".equals(action) || "delete".equals(action)) {
            EntityManager em = EMF.getEM();
            try {
                HallServiceImpl hallService = new HallServiceImpl(em);
                em.getTransaction().begin();
                Result<Hall> hallResult = hallService.getOneById(idResult.getData());
                if (!hallResult.isSuccess()) {
                    log.error("Erreur hall non trouvé ");
                    ServletUtils.forwardWithError(request, response, "erreur hall non trouvé.", HALL_JSP, TEMPLATE);
                    return;
                }
                hallResult.getData().setActive(ServletUtils.changeActive(hallResult.getData().isActive()));
                hallService.update(hallResult.getData());
                em.getTransaction().commit();
                log.info("hall " + hallResult.getData().getId() + " activé/softDelete avec succès");
                ServletUtils.redirectWithMessage(request, response, "hall activé/softDelete avec succès", "success", "/hall");
                return;
            } catch (Exception e) {
                log.error("Erreur d'activation/softDelete : " + e.getMessage());
                ServletUtils.forwardWithError(request, response, e.getMessage(), HALL_JSP, TEMPLATE);
            } finally {
                if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
                if (em != null) em.close();
            }
            return;
            //create -- edit
        } else {
            //Create
            //Verification des champs
            if (idParam == null) {
                Result<Hall> baseResult = HallBusiness.initCreateForm(
                        request.getParameter("hallName"),
                        request.getParameter("width"),
                        request.getParameter("length"),
                        request.getParameter("height"),
                        request.getParameter("active")
                );
                if (!baseResult.isSuccess()) {
                    log.error("Erreur lors de la " + (idParam == null ? "création" : "édition") + "du hall : " + baseResult.getErrors());
                    ServletUtils.forwardWithErrors(request, response, baseResult.getErrors(), HALL_FORM_JSP, TEMPLATE);
                    return;
                } else {
                    EntityManager em = EMF.getEM();
                    try {
                        HallServiceImpl hallService = new HallServiceImpl(em);
                        em.getTransaction().begin();
                        hallService.create(baseResult.getData());
                        em.getTransaction().commit();
                        log.info("Hall créé avec success.");
                        ServletUtils.redirectWithMessage(request, response, "hall créé avec success.", "success", "/hall");
                        return;
                    } catch (Exception e) {
                        log.error("Une erreur est survenue lors de la création du hall. " + e.getMessage());
                        ServletUtils.forwardWithError(request, response, "Une erreur est survenue lors de la création du hall. ", HALL_FORM_JSP, TEMPLATE);
                        return;
                    } finally {
                        if (em.getTransaction().isActive()) {
                            em.getTransaction().rollback();
                        }
                        if (em != null) em.close();
                    }
                }
                //Edit
            } else {
                Result<HallUpdateForm> hallUpdateFormResult = HallBusiness.initUpdateForm(
                        request.getParameter("hallName"),
                        request.getParameter("width"),
                        request.getParameter("length"),
                        request.getParameter("height"),
                        request.getParameter("active")
                );
                EntityManager em = EMF.getEM();
                try{
                    HallServiceImpl hallService = new HallServiceImpl(em);
                    em.getTransaction().begin();
                    Result<Hall> hall = hallService.getOneById(idResult.getData());
                    if(!hall.isSuccess()){
                        log.error("Hall :" + idResult.getData() + " n'existe pas");
                        ServletUtils.forwardWithError(request,response,"Le hall n'existe pas",HALL_FORM_JSP, TEMPLATE);
                        return;
                    }
                    HallMapper.fromUpdateForm(hallUpdateFormResult.getData(),hall.getData());
                    hallService.update(hall.getData());
                    em.getTransaction().commit();
                    log.info("Hall modifié avec success.");
                    ServletUtils.redirectWithMessage(request, response, "Hall modifié avec success.", "success", "/hall");
                    return;
                }catch (Exception e){
                    log.error("Erreur lors de l'update du hall", e);
                    ServletUtils.forwardWithError(request, response, "Erreur lors de l'update du hall", HALL_FORM_JSP, TEMPLATE);
                    return;
                }finally {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    if (em != null) em.close();
                }
            }
        }


    }
}