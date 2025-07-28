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

    public static void handleFormDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtils.forwardWithContent(request, response, HALL_FORM_JSP, TEMPLATE);
    }

    public static void handleEditForm(HttpServletRequest request, HttpServletResponse response, Hall hall) throws ServletException, IOException {
        request.setAttribute("hallName", hall.getHallName());
        request.setAttribute("hallHeight", hall.getHeight());
        request.setAttribute("hallLength", hall.getLength());
        request.setAttribute("hallWidth", hall.getWidth());
        request.setAttribute("hallActive", hall.isActive());
        request.setAttribute("hallId", hall.getId());

        ServletUtils.forwardWithContent(request, response, HALL_FORM_JSP, TEMPLATE);
    }

    public static void handleList(HttpServletRequest request, HttpServletResponse response, List<Hall> halls) throws ServletException, IOException {
        request.setAttribute("halls", halls);
        ServletUtils.forwardWithContent(request, response, HALL_JSP, TEMPLATE);
    }
}
