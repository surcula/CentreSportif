package controllers.helpers;

import business.ServletUtils;
import entities.SportField;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static constants.Rooting.*;

public class SportFieldControllerHelper {


    /**
     * Load SportField FORM CREATE
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public static void handleFormDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtils.forwardWithContent(request, response, SPORTFIELD_FORM_JSP, TEMPLATE);
    }

    /**
     * Load SportField Page
     * @param request
     * @param response
     * @param sportsFields
     * @throws ServletException
     * @throws IOException
     */
    public static void handleList(HttpServletRequest request, HttpServletResponse response, List<SportField> sportsFields) throws ServletException, IOException {
        request.setAttribute("sportsfields", sportsFields);
        ServletUtils.forwardWithContent(request, response, SPORTFIELD_JSP, TEMPLATE);
    }
    /**
     * Load SportField Form EDIT
     * @param request
     * @param response
     * @param sportField
     * @throws ServletException
     * @throws IOException
     */
    public static void handleEditForm(HttpServletRequest request, HttpServletResponse response, SportField sportField) throws ServletException, IOException {
        request.setAttribute("sportfield", sportField);
        ServletUtils.forwardWithContent(request, response, SPORTFIELD_FORM_JSP, TEMPLATE);
    }

}
