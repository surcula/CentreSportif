package servlets;

import Tools.ParamUtils;
import Tools.Result;
import business.ServletUtils;
import business.SportFieldBusiness;
import controllers.helpers.SportFieldControllerHelper;
import dto.EMF;
import dto.Page;
import entities.SportField;
import enums.Scope;
import services.SportFieldServiceImpl;

import javax.persistence.EntityManager;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

import static constants.Rooting.*;

@WebServlet(name = "SportFieldServlet", value = "/sports-fields")
public class SportFieldServlet extends HttpServlet {


    // Log4j
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SportFieldServlet.class);



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false); //Retourne une session si existante
        String form = request.getParameter("form");
        String editForm = request.getParameter("editForm");
        String role = (session != null && session.getAttribute("role") != null)
                ? session.getAttribute("role").toString()
                : null;
        boolean fullAccess = ServletUtils.isFullAuthorized(role);
        if (!fullAccess) {
            ServletUtils.redirectNoAuthorized(request, response);
            return;
        }
        // --- Formulaire de création ---
        if (form != null) {

            SportFieldControllerHelper.handleFormDisplay(request, response);
            return;
            // --- Formulaire d'édition ---
        } else if (editForm != null) {
            Result<Integer> sportFieldUpdateId = ParamUtils.verifyId(editForm);
            if (!sportFieldUpdateId.isSuccess()) {
                log.error("Erreur lors de la conversion de l'id du sport-field" );
                ServletUtils.forwardWithError(request, response, "Erreur lors de la conversion de l'id du sport-field", SPORTFIELD_JSP, TEMPLATE);
                return;
            }
            EntityManager em = EMF.getEM();
            try {
                SportFieldServiceImpl sportFieldService = new SportFieldServiceImpl(em);
                Result<SportField> result = sportFieldService.getOneById(sportFieldUpdateId.getData());

                if (result.isSuccess()) {
                    SportFieldControllerHelper.handleEditForm(request, response, result.getData());
                    return;
                } else {
                    ServletUtils.forwardWithErrors(request, response, result.getErrors(), SPORTFIELD_FORM_JSP, TEMPLATE);
                    return;
                }
            } catch (Exception e) {
                log.error("Erreur lors du chargement du formulaire du sportField", e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), SPORTFIELD_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null) em.close();
            }
        } else {
            // --- Liste paginée ---
            EntityManager em = EMF.getEM();
            try {
                SportFieldServiceImpl sportFieldService = new SportFieldServiceImpl(em);
                SportFieldBusiness sportFieldBusiness = new SportFieldBusiness(sportFieldService);

                Result<Integer> pageRes = ParamUtils.stringToInteger(request.getParameter("page"));
                int page = pageRes.isSuccess() ? Math.max(1, pageRes.getData()) : 1;

                Result<Integer> sizeRes = ParamUtils.stringToInteger(request.getParameter("size"));
                int size = sizeRes.isSuccess() ? Math.min(20, Math.max(1, sizeRes.getData())) : 10;

                Result<Page<SportField>> result = fullAccess
                        ? sportFieldBusiness.getAllSportFieldPaged(page, size, Scope.ALL)
                        : sportFieldBusiness.getAllSportFieldPaged(page, size, Scope.ACTIVE);

                if (result.isSuccess()) {
                    Page<SportField> p = result.getData();
                    request.setAttribute("sportsfields", p.getContent());
                    request.setAttribute("page", p.getPage());
                    request.setAttribute("size", p.getSize());
                    request.setAttribute("totalPages", p.getTotalPages());
                    request.setAttribute("totalElements", p.getTotalElements());
                    request.setAttribute("fullAccess", fullAccess);
                    SportFieldControllerHelper.handleList(request, response, p.getContent());

                } else {
                    ServletUtils.forwardWithErrors(request, response, result.getErrors(), SPORTFIELD_JSP, TEMPLATE);
                    return;
                }
            } catch (Exception e) {
                log.error("Erreur doGet sportField", e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), SPORTFIELD_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null) em.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
 