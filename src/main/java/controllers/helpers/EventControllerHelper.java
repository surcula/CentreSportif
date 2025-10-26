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
    /**
     * Forwards to the event form JSP in creation mode,
     * attaching the list of available event for selection
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public static void handleFormDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtils.forwardWithContent(request, response, EVENT_FORM_JSP, TEMPLATE);
    }

    /**
     * Forwards to the event Form JSP in edit mode,
     * attaching the current event and the list of available event.
     * @param request
     * @param response
     * @param event
     * @throws ServletException
     * @throws IOException
     */
    public static void handleEditForm(HttpServletRequest request, HttpServletResponse response, Event event) throws ServletException, IOException {
        request.setAttribute("event", event);
        request.setAttribute("eventName", event.getEventName());
        request.setAttribute("eventBeginDateHour", event.getBeginDateHour());
        request.setAttribute("eventEndDateHour", event.getEndDateHour());
        request.setAttribute("eventInfo", event.getInfo());
        request.setAttribute("eventPicture", event.getPicture());
        request.setAttribute("eventActive", event.isActive());
        request.setAttribute("eventId", event.getId());
        ServletUtils.forwardWithContent(request, response, EVENT_FORM_JSP, TEMPLATE);
    }

    /**
     * Forwards to the event list
     * @param request
     * @param response
     * @param events
     * @throws ServletException
     * @throws IOException
     */
    public static void handleList(HttpServletRequest request, HttpServletResponse response, List<Event> events) throws ServletException, IOException {
        request.setAttribute("events", events);
        ServletUtils.forwardWithContent(request, response, EVENT_JSP, TEMPLATE);
        //request.getRequestDispatcher("/views/template/template.jsp").forward(request, response);
        System.out.println(">>>>>>>>>> \n\n===========================================");
        System.out.println(">>>>>>>>>> handlelist est appelé");
        System.out.println("===========================\n");
    }
}
