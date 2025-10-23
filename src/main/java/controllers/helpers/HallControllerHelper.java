package controllers.helpers;

import business.ServletUtils;
import entities.Hall;
import static constants.Rooting.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class HallControllerHelper {

    /**
     * Load HAll FORM CREATE
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public static void handleFormDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtils.forwardWithContent(request, response, HALL_FORM_JSP, TEMPLATE);
        return;
    }

    /**
     * Load Hall Form EDIT
     * @param request
     * @param response
     * @param hall
     * @throws ServletException
     * @throws IOException
     */
    public static void handleEditForm(HttpServletRequest request, HttpServletResponse response, Hall hall) throws ServletException, IOException {
        request.setAttribute("hall", hall);
        ServletUtils.forwardWithContent(request, response, HALL_FORM_JSP, TEMPLATE);
        return;
    }

    /**
     * Load Hall Page
     * @param request
     * @param response
     * @param halls
     * @throws ServletException
     * @throws IOException
     */
    public static void handleList(HttpServletRequest request, HttpServletResponse response, List<Hall> halls) throws ServletException, IOException {
        request.setAttribute("halls", halls);
        ServletUtils.forwardWithContent(request, response, HALL_JSP, TEMPLATE);
        return;
    }
}
