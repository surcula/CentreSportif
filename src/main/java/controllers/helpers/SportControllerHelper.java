package controllers.helpers;

import business.ServletUtils;
import entities.Sport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static constants.Rooting.*;
import static constants.Rooting.TEMPLATE;

public class SportControllerHelper {

    /**
     * Load Sport FORM CREATE
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public static void handleFormDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtils.forwardWithContent(request, response, SPORT_FORM_JSP, TEMPLATE);
    }

    /**
     * Load Sport Form EDIT
     * @param request
     * @param response
     * @param sport
     * @throws ServletException
     * @throws IOException
     */
    public static void handleEditForm(HttpServletRequest request, HttpServletResponse response, Sport sport) throws ServletException, IOException {
        request.setAttribute("sport", sport);
        ServletUtils.forwardWithContent(request, response, SPORT_FORM_JSP, TEMPLATE);
    }

    /**
     * Load Sport Page
     * @param request
     * @param response
     * @param sports
     * @throws ServletException
     * @throws IOException
     */
    public static void handleList(HttpServletRequest request, HttpServletResponse response, List<Sport> sports) throws ServletException, IOException {
        request.setAttribute("sports", sports);
        ServletUtils.forwardWithContent(request, response, SPORT_JSP, TEMPLATE);
    }

}
