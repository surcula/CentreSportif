package controllers.helpers;

import business.ServletUtils;
import entities.Event;
import static constants.Rooting.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class EventControllerHelper {
    public static void handleFormDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtils.forwardWithContent(request, response, EVENT_FORM_JSP, TEMPLATE);
    }

    public static void handleEditForm(HttpServletRequest request, HttpServletResponse response, Event event) throws ServletException, IOException {
        request.setAttribute("eventName", event.getEventName());
        request.setAttribute("eventBeginDateHour", event.getBeginDateHour());
        request.setAttribute("eventEndDateHour", event.getEndDateHour());
        request.setAttribute("eventInfo", event.getInfo());
        request.setAttribute("eventPicture", event.getPicture());
        request.setAttribute("eventActive", event.isActive());
        request.setAttribute("eventId", event.getId());
        ServletUtils.forwardWithContent(request, response, EVENT_FORM_JSP, TEMPLATE);
    }

    public static void handleList(HttpServletRequest request, HttpServletResponse response, List<Event> events) throws ServletException, IOException {
        request.setAttribute("events", events);
        //ServletUtils.forwardWithContent(request, response, EVENT_JSP, TEMPLATE);
        request.getRequestDispatcher("/views/template/template.jsp").forward(request, response);
        System.out.println(">>>>>>>>>> \n\n===========================================");
        System.out.println(">>>>>>>>>> handlelist est appelé");
        System.out.println("===========================\n");
    }
}
