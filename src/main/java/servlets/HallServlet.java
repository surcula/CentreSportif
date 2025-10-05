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
                } else {
                    ServletUtils.forwardWithErrors(request, response, result.getErrors(), HALL_FORM_JSP, TEMPLATE);
                    return;
                }
            } catch (Exception e) {
                log.error("Erreur lors du chargement du formulaire du hall", e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), HALL_JSP, TEMPLATE);
                return;
            } finally {
                em.close();
            }
            return;
        } else {
            // --- Liste paginée ---
            EntityManager em = EMF.getEM();
            try {
                HallServiceImpl hallService = new HallServiceImpl(em);
                HallBusiness hallBusiness = new HallBusiness(hallService);

                Result<Integer> pageRes = ParamUtils.stringToInteger(request.getParameter("page"));
                int page = pageRes.isSuccess() ? Math.max(1, pageRes.getData()) : 1;

                Result<Integer> sizeRes = ParamUtils.stringToInteger(request.getParameter("size"));
                int size = sizeRes.isSuccess() ? Math.min(10, Math.max(1, sizeRes.getData())) : 10;

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
                em.close();
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

        //Activer un hall désactivé
        if ("activer".equals(request.getParameter("action"))) {
            EntityManager em = EMF.getEM();
            try {
                int id = Integer.parseInt(request.getParameter("hallId"));
                HallServiceImpl hallService = new HallServiceImpl(em);
                em.getTransaction().begin();
                Result<Hall> result = hallService.getOneById(id);
                if (!result.isSuccess()) {
                    throw new Exception("Hall introuvable");
                }

                Hall hall = result.getData();
                hall.setActive(ServletUtils.changeActive(hall.isActive()));
                hallService.update(hall);

                em.getTransaction().commit();

                log.info("Hall " + hall.getId() + " activé avec succès");
                ServletUtils.redirectWithMessage(request, response, "Hall activé avec succès", "success", "/hall");
                return;
            } catch (Exception e) {
                log.error("Erreur d'activation : " + e.getMessage());
                ServletUtils.forwardWithError(request, response, e.getMessage(), HALL_JSP, TEMPLATE);
            } finally {
                if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
                if (em != null) em.close();
            }
            return;
        }

        //Delete ?
        if ("delete".equals(request.getParameter("action"))) {
            EntityManager em = EMF.getEM();
            try {
                int id = Integer.parseInt(request.getParameter("hallId"));
                HallServiceImpl hallService = new HallServiceImpl(em);
                em.getTransaction().begin();


                Result<Hall> result = hallService.getOneById(id);
                if (!result.isSuccess()) {
                    throw new Exception("Hall introuvable");
                }

                Hall hall = result.getData();
                hall.setActive(ServletUtils.changeActive(hall.isActive()));
                hallService.update(hall);

                em.getTransaction().commit();

                log.info("Hall " + hall.getId() + " désactivé avec succès");
                ServletUtils.redirectWithMessage(request, response, "Hall supprimé avec succès", "success", "/hall");

            } catch (Exception e) {
                log.error("Erreur suppression : " + e.getMessage());
                ServletUtils.forwardWithError(request, response, e.getMessage(), HALL_JSP, TEMPLATE);
            } finally {
                if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
                if (em != null) em.close();
            }
            return;
        }

        //Verification des champs
        Result<Hall> baseResult = HallBusiness.initCreateForm(
                request.getParameter("hallName"),
                request.getParameter("width"),
                request.getParameter("length"),
                request.getParameter("height"),
                request.getParameter("active")
        );

        if (baseResult.isSuccess()) {

            //edit ou create ?
            if (request.getParameter("hallId") != null) {
                EntityManager em = EMF.getEM();
                try {
                    // Création dto
                    HallUpdateForm hallUpdateForm = new HallUpdateForm(
                            Integer.parseInt(request.getParameter("hallId")),
                            baseResult.getData().getHallName(),
                            baseResult.getData().isActive(),
                            baseResult.getData().getHeight(),
                            baseResult.getData().getLength(),
                            baseResult.getData().getWidth()

                    );
                    HallServiceImpl hallService = new HallServiceImpl(em);
                    em.getTransaction().begin();
                    hallService.update(HallMapper.fromUpdateForm(hallUpdateForm));
                    em.getTransaction().commit();
                    log.info("Hall " + hallUpdateForm.getId() + " modified successfully");
                    ServletUtils.redirectWithMessage(
                            request,
                            response,
                            "Hall modifié avec succès",
                            "success",
                            "/hall"
                    );
                } catch (NumberFormatException e) {
                    log.error("Id du hall invalide" + e.getMessage());
                    ServletUtils.forwardWithError(
                            request,
                            response,
                            "Id du hall invalide",
                            HALL_FORM_JSP,
                            TEMPLATE
                    );
                } catch (Exception e) {
                    log.error(e.getMessage());
                    ServletUtils.forwardWithError(
                            request,
                            response,
                            e.getMessage(),
                            HALL_FORM_JSP,
                            TEMPLATE
                    );
                } finally {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    em.close();
                }
            } else {
                EntityManager em = EMF.getEM();
                try {
                    HallServiceImpl hallService = new HallServiceImpl(em);
                    em.getTransaction().begin();
                    hallService.create(baseResult.getData());
                    em.getTransaction().commit();
                    log.info("Hall created successfully");
                    ServletUtils.redirectWithMessage(
                            request,
                            response,
                            "Hall créé avec succès",
                            "success",
                            "/hall"
                    );
                } catch (Exception e) {
                    log.error(e.getMessage());
                    ServletUtils.forwardWithError(
                            request,
                            response,
                            e.getMessage(),
                            HALL_FORM_JSP,
                            TEMPLATE
                    );
                } finally {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    em.close();
                }
            }
        } else {
            ServletUtils.forwardWithErrors(
                    request,
                    response,
                    baseResult.getErrors(),
                    HALL_FORM_JSP,
                    TEMPLATE
            );

        }

    }
}