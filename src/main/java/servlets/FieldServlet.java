
package servlets;

import Tools.ParamUtils;
import Tools.Result;
import business.FieldBusiness;
import business.HallBusiness;
import business.ServletUtils;
import business.ValidateForm;
import controllers.helpers.FieldControllerHelper;
import dto.EMF;
import dto.FieldUpdateForm;
import dto.Page;
import entities.Field;
import entities.Hall;
import enums.Scope;
import interfaces.HallService;
import mappers.FieldMapper;
import services.FieldServiceImpl;
import services.HallServiceImpl;

import javax.persistence.EntityManager;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.xml.bind.ValidationEvent;
import java.io.IOException;

import static constants.Rooting.*;

@WebServlet(name = "Field", value = "/field")
public class FieldServlet extends HttpServlet {

    // Log4j
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FieldServlet.class);

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
            EntityManager em = EMF.getEM();
            try {
                FieldControllerHelper.handleFormDisplay(request, response, em);
            } catch (Exception e) {
                log.error(e.getMessage());
                ServletUtils.forwardWithError(request, response, "Erreur lors du chargement du formulaire de terrain", HOME_JSP, TEMPLATE);
                return;
            } finally {
                em.close();
            }
            // --- Formulaire d'édition ---
        } else if (editForm != null) {
            if (!fullAccess) {
                ServletUtils.redirectNoAuthorized(request, response);
                return;
            }
            Result<Integer> fieldUpdateId = ParamUtils.verifyId(editForm);
            if (!fieldUpdateId.isSuccess()) {
                log.error("Erreur lors de la conversion de l'id du field");
                ServletUtils.forwardWithError(request, response, "Erreur lors de la conversion de l'id du field", FIELD_JSP, TEMPLATE);
                return;
            }
            EntityManager em = EMF.getEM();
            try {
                FieldServiceImpl fieldService = new FieldServiceImpl(em);
                Result<Field> result = fieldService.getOneById(fieldUpdateId.getData());
                if (result.isSuccess()) {
                    FieldControllerHelper.handleEditForm(request, response, result.getData(), em);
                } else {
                    ServletUtils.forwardWithErrors(request, response, result.getErrors(), FIELD_FORM_JSP, TEMPLATE);
                    return;
                }
            } catch (Exception e) {
                log.error("Erreur lors du chargement du terrain en édition", e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), FIELD_JSP, TEMPLATE);
                return;
            } finally {
                em.close();
            }
        } else {
            EntityManager em = EMF.getEM();
            try {
                FieldServiceImpl fieldService = new FieldServiceImpl(em);
                FieldBusiness fieldBusiness = new FieldBusiness(fieldService);

                Result<Integer> pageRes = ParamUtils.stringToInteger(request.getParameter("page"));
                int page = pageRes.isSuccess() ? Math.max(1, pageRes.getData()) : 1;

                Result<Integer> sizeRes = ParamUtils.stringToInteger(request.getParameter("size"));
                int size = sizeRes.isSuccess() ? Math.min(10, Math.max(1, sizeRes.getData())) : 10;

                Result<Page<Field>> result = fullAccess
                        ? fieldBusiness.getAllFieldsPaged(page, size, Scope.ALL)
                        : fieldBusiness.getAllFieldsPaged(page, size, Scope.ACTIVE);

                if (result.isSuccess()) {
                    Page<Field> p = result.getData();
                    request.setAttribute("fields", p.getContent());
                    request.setAttribute("page", p.getPage());
                    request.setAttribute("size", p.getSize());
                    request.setAttribute("totalPages", p.getTotalPages());
                    request.setAttribute("totalElements", p.getTotalElements());
                    request.setAttribute("fullAccess", fullAccess);

                    FieldControllerHelper.handleList(request, response, p.getContent());
                } else {
                    ServletUtils.forwardWithErrors(request, response, result.getErrors(), FIELD_JSP, TEMPLATE);
                    return;
                }
            } catch (Exception e) {
                log.error("Erreur doGet fields", e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), FIELD_JSP, TEMPLATE);
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
        String action = request.getParameter("action");
        String idParam = request.getParameter("fieldId");

        //On vérifie l'id si c'est possible ou non.
        Result<Integer> idResult = ParamUtils.verifyId(idParam);
        if (idParam != null) {

            if (!idResult.isSuccess()) {
                ServletUtils.forwardWithErrors(request, response, idResult.getErrors(), FIELD_JSP, TEMPLATE);
                return;
            }
        }

        //Activer ou softDelete un field
        if ("activer".equals(action) || "delete".equals(action)) {
            EntityManager em = EMF.getEM();
            try {
                FieldServiceImpl fieldService = new FieldServiceImpl(em);
                em.getTransaction().begin();
                Result<Field> result = fieldService.getOneById(idResult.getData());
                if (!result.isSuccess()) {
                    log.error("Erreur field non trouvé ");
                    ServletUtils.forwardWithError(request, response, "erreur field non trouvé.", FIELD_FORM_JSP, TEMPLATE);
                    return;
                }
                Field field = result.getData();
                field.setActive(ServletUtils.changeActive(field.isActive()));
                fieldService.update(field);
                em.getTransaction().commit();
                log.info("field " + field.getId() + " activé/softDelete avec succès");
                ServletUtils.redirectWithMessage(request, response, "field activé/softDelete avec succès", "success", "/field");
                return;
            } catch (Exception e) {
                log.error("Erreur d'activation/softDelete : " + e.getMessage());
                ServletUtils.forwardWithError(request, response, e.getMessage(), FIELD_JSP, TEMPLATE);
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
                Result<Field> baseResult = FieldBusiness.initCreateForm(
                        request.getParameter("fieldName"),
                        request.getParameter("hallId"),
                        request.getParameter("active")
                );
                if (!baseResult.isSuccess()) {
                    log.error("Erreur lors de la " + (idParam == null ? "création" : "édition") + "du field : " + baseResult.getErrors());
                    ServletUtils.forwardWithErrors(request, response, baseResult.getErrors(), FIELD_FORM_JSP, TEMPLATE);
                    return;
                } else {
                    Result<Integer> hallId = ParamUtils.verifyId(request.getParameter("hallId"));
                    if (!hallId.isSuccess() || hallId.getData() == null) {
                        log.error("Erreur id du hall incorrect ");
                        ServletUtils.forwardWithError(request, response, "erreur id du hall incorrect", FIELD_FORM_JSP, TEMPLATE);
                        return;
                    }
                    EntityManager em = EMF.getEM();
                    try {
                        HallServiceImpl hallService = new HallServiceImpl(em);
                        FieldServiceImpl fieldService = new FieldServiceImpl(em);
                        em.getTransaction().begin();
                        Result<Hall> hall = hallService.getOneById(hallId.getData());
                        baseResult.getData().setHall(hall.getData());
                        fieldService.create(baseResult.getData());
                        em.getTransaction().commit();
                        log.info("Field créé avec success.");
                        ServletUtils.redirectWithMessage(request, response, "field avec success.", "success", "/field");
                        return;
                    } catch (Exception e) {
                        log.error("Une erreur est survenue lors de la création du field. " + e.getMessage());
                        ServletUtils.forwardWithError(request, response, "Une erreur est survenue lors de la création du field. ", HALL_FORM_JSP, TEMPLATE);
                    } finally {
                        if (em.getTransaction().isActive()) {
                            em.getTransaction().rollback();
                        }
                        em.close();
                    }
                }
                //Edit
            } else {
                Result<FieldUpdateForm> baseResult = FieldBusiness.initUpdateForm(
                        request.getParameter("fieldName"),
                        request.getParameter("hallId"),
                        request.getParameter("active")
                );
                EntityManager em = EMF.getEM();
                try{
                    FieldServiceImpl fieldService = new FieldServiceImpl(em);
                    em.getTransaction().begin();
                    HallServiceImpl hallService = new HallServiceImpl(em);
                    Result<Hall> hall = hallService.getOneById(baseResult.getData().getHallId());
                    if(!hall.isSuccess()){
                        log.error("Hall :" + idResult.getData() + " n'existe pas");
                        ServletUtils.forwardWithError(request,response,"Le hall n'existe pas",FIELD_FORM_JSP, TEMPLATE);
                    }
                    Result<Field> field = fieldService.getOneById(idResult.getData());
                    FieldMapper.fromUpdateForm(baseResult.getData() , hall.getData(), field.getData());
                    fieldService.update(field.getData());
                    em.getTransaction().commit();
                    log.info("Field modifié avec success.");
                    ServletUtils.redirectWithMessage(request, response, "field modifié avec success.", "success", "/field");
                    return;
                }catch (Exception e){
                    log.error("Erreur lors de l'update du field", e);
                    ServletUtils.forwardWithError(request, response, "Erreur lors de l'update du field", FIELD_FORM_JSP, TEMPLATE);
                }finally {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    em.close();
                }
            }
        }

    }
}

