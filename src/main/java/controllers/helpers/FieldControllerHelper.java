package controllers.helpers;

import Tools.Result;
import business.ServletUtils;
import entities.Field;
import entities.Hall;
import services.HallServiceImpl;
import servlets.HallServlet;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static constants.Rooting.*;

public class FieldControllerHelper {

    // Log4j
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FieldControllerHelper.class);


    /**
     * Forwards to the Field form JSP in creation mode,
     * attaching the list of available Halls for selection.
     * @param request
     * @param response
     * @param em
     * @throws ServletException
     * @throws IOException
     */
    public static void handleFormDisplay(HttpServletRequest request, HttpServletResponse response,EntityManager em) throws ServletException, IOException {
        attachHalls(request,em);
        ServletUtils.forwardWithContent(request, response, FIELD_FORM_JSP, TEMPLATE);
    }

    public static void handleList(HttpServletRequest request, HttpServletResponse response, List<Field> fields) throws ServletException, IOException {
        request.setAttribute("fields", fields);
        ServletUtils.forwardWithContent(request, response, FIELD_JSP, TEMPLATE);
    }

    /**
     * Forwards to the Field form JSP in edit mode,
     * attaching the current Field and the list of available Halls.
     * @param request
     * @param response
     * @param field
     * @param em
     * @throws ServletException
     * @throws IOException
     */
    public static void handleEditForm(HttpServletRequest request, HttpServletResponse response, Field field, EntityManager em) throws ServletException, IOException {
        attachHalls(request,em);
        request.setAttribute("field", field);
        ServletUtils.forwardWithContent(request, response, FIELD_FORM_JSP, TEMPLATE);
    }

    /**
     * attachHalls at response
     * @param request
     * @param em
     * @throws ServletException
     * @throws IOException
     */
    private static void attachHalls(HttpServletRequest request, EntityManager em) throws ServletException {
        Result<List<Hall>> halls = getHalls(em);
        if(halls.isSuccess()){
            request.setAttribute("halls", halls.getData());
        }else{
            request.setAttribute("errors", halls.getErrors());
        }
    }

    /**
     * Load Halls for form
     * @param em
     * @return
     */
    private static Result<List<Hall>> getHalls(EntityManager em)  {
        HallServiceImpl hallService = new HallServiceImpl(em);
        Result<List<Hall>> halls = hallService.getAllHalls(0,0);
        if(halls.isSuccess()){
            log.info("Chargement de la liste de halls pour le formulaire : OK");
            return halls;
        }else {
            Map<String, String> errors = new HashMap<>();
            errors.put("halls", "Impossible de charger la liste des halls");
            return Result.fail(errors);
        }
    }
}
