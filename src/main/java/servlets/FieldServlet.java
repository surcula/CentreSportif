
package servlets;

import Tools.ParamUtils;
import Tools.Result;
import business.FieldBusiness;
import business.ServletUtils;
import controllers.helpers.FieldControllerHelper;
import dto.EMF;
import dto.Page;
import entities.Field;
import enums.Scope;
import services.FieldServiceImpl;
import javax.persistence.EntityManager;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
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
                ServletUtils.redirectNoAuthorized(request,response);
                return;
            }
            EntityManager em = EMF.getEM();
            try{
                FieldControllerHelper.handleFormDisplay(request, response,em);
            }catch (Exception e){
                log.error(e.getMessage());
                ServletUtils.forwardWithError(request,response,"Erreur lors du chargement du formulaire de terrain",HOME_JSP,TEMPLATE);
                return;
            }finally {
                em.close();
            }
        // --- Formulaire d'édition ---
        } else if (editForm != null) {
            if (!fullAccess) {
                ServletUtils.redirectNoAuthorized(request,response);
                return;
            }
            Result<Integer> fieldUpdateId = ParamUtils.verifyId(editForm);
            if(!fieldUpdateId.isSuccess()){
                log.error("Erreur lors de la conversion de l'id du field");
                ServletUtils.forwardWithError(request, response,"Erreur lors de la conversion de l'id du field", FIELD_JSP, TEMPLATE);
                return;
            }
            EntityManager em = EMF.getEM();
            try {
                FieldServiceImpl fieldService = new FieldServiceImpl(em);
                Result<Field> result = fieldService.getOneById(fieldUpdateId.getData());
                if (result.isSuccess()) {
                    FieldControllerHelper.handleEditForm(request, response, result.getData(),em);
                } else {
                    ServletUtils.forwardWithErrors(request, response, result.getErrors(), FIELD_FORM_JSP, TEMPLATE);
                    return;
                }
            } catch (Exception e) {
                log.error("Erreur lors du chargement du terrain en édition",e);
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

    }
}

 