package servlets;

import Tools.Result;
import business.HallBusiness;
import business.ServletUtils;
import controllers.helpers.HallControllerHelper;
import dto.EMF;
import dto.HallCreateForm;
import dto.HallUpdateForm;
import entities.Hall;
import mappers.HallMapper;
import org.apache.log4j.Logger;
import services.HallServiceImpl;

import static constants.Rooting.*;

import javax.mail.Session;
import javax.persistence.EntityManager;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Hall", value = "/hall")
public class HallServlet extends HttpServlet {


    private EntityManager em;
    private HallServiceImpl hallService;
    // Log4j
    private static Logger log = Logger.getLogger(HallServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        em = EMF.getEM();
        HttpSession session = request.getSession(false); //Retourne une session si existante

        String form = request.getParameter("form");
        String editForm = request.getParameter("editForm");

        if (form != null) {

            HallControllerHelper.handleFormDisplay(request, response);
        } else if (editForm != null) {
            try {
                HallServiceImpl hallService = new HallServiceImpl(em);
                Result<Hall> result = hallService.getOneById(Integer.parseInt(editForm));
                if (result.isSuccess()) {
                    HallControllerHelper.handleEditForm(request, response, result.getData());
                } else {
                    ServletUtils.forwardWithErrors(request, response, result.getErrors(), HALL_FORM_JSP, TEMPLATE);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                ServletUtils.forwardWithError(request, response, e.getMessage(), HALL_JSP, TEMPLATE);
            } finally {
                em.close();
            }
        } else {
            try {
                HallServiceImpl hallService = new HallServiceImpl(em);
                Result<List<Hall>> result = hallService.getAllActiveHalls();
                if (result.isSuccess()) {
                    HallControllerHelper.handleList(request, response, result.getData());
                } else {
                    ServletUtils.forwardWithErrors(request, response, result.getErrors(), HALL_JSP, TEMPLATE);
                }

            } catch (Exception e) {
                log.error(e.getMessage());
                ServletUtils.forwardWithError(request, response, e.getMessage(), HALL_JSP, TEMPLATE);
            } finally {
                em.close();
            }
        }

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Vérifier les droits
        HttpSession session = request.getSession(false);
        if (!(session != null && ServletUtils.hasRole(session.getAttribute("role").toString())) ) {
            ServletUtils.forwardWithError(
                    request,
                    response,
                    "Vous n'avez pas les droits. ",
                    HOME_JSP,
                    TEMPLATE
            );
            return;
        }

        //Activer un hall désactivé
        if ("activer".equals(request.getParameter("action"))) {
            try {
                int id = Integer.parseInt(request.getParameter("hallId"));
                em = EMF.getEM();
                hallService = new HallServiceImpl(em);
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
                ServletUtils.redirectWithSucces(request, response, "Hall activé avec succès", "/hall");

            } catch (Exception e) {
                log.error("Erreur d'activation : " + e.getMessage());
                ServletUtils.forwardWithError(request, response, e.getMessage(), HALL_JSP, TEMPLATE);
            } finally {
                if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
                if (em != null) em.close();
            }
            return; // pour ne pas continuer sur le reste du doPost
        }

        //Delete ?
        if ("delete".equals(request.getParameter("action"))) {
            try {
                int id = Integer.parseInt(request.getParameter("hallId"));
                em = EMF.getEM();
                hallService = new HallServiceImpl(em);
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
                ServletUtils.redirectWithSucces(request, response, "Hall supprimé avec succès", "/hall");

            } catch (Exception e) {
                log.error("Erreur suppression : " + e.getMessage());
                ServletUtils.forwardWithError(request, response, e.getMessage(), HALL_JSP, TEMPLATE);
            } finally {
                if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
                if (em != null) em.close();
            }
            return; // pour ne pas continuer sur le reste du doPost
        }


        //Verification des champs
        Result<HallCreateForm> baseResult = HallBusiness.initCreateForm(
                request.getParameter("hallName"),
                request.getParameter("width"),
                request.getParameter("length"),
                request.getParameter("height"),
                request.getParameter("active")
        );

        if (baseResult.isSuccess()) {

            //edit ou create ?
            if (request.getParameter("hallId") != null) {
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
                    em = EMF.getEM();
                    hallService = new HallServiceImpl(em);
                    em.getTransaction().begin();
                    hallService.update(HallMapper.fromUpdateForm(hallUpdateForm));
                    em.getTransaction().commit();
                    log.info("Hall " + hallUpdateForm.getId() + " modified successfully");
                    ServletUtils.redirectWithSucces(
                            request,
                            response,
                            "Hall modifié avec succès",
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
                try {
                    em = EMF.getEM();
                    hallService = new HallServiceImpl(em);
                    em.getTransaction().begin();
                    hallService.create(baseResult.getData());
                    em.getTransaction().commit();
                    log.info("Hall created successfully");
                    ServletUtils.redirectWithSucces(
                            request,
                            response,
                            "Hall créé avec succès",
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
