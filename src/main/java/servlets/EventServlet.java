package servlets;

import business.ServletUtils;

import javax.persistence.EntityManager;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dto.EMF;
import entities.Event;
/**
 * The class to view the JSP form page and to send the data of the form in DB
 * @author Franz
 *
 */
@MultipartConfig
@WebServlet(name = "EventServlet", value = "/event")
public class EventServlet extends HttpServlet {
    //private EntityManagerFactory entityManagerFactory;
    /**
     * Method to view the form
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String eventForm = request.getParameter("form");
        String editEventForm = request.getParameter("editForm");

        if("true".equals(eventForm)) {
            ServletUtils.forwardWithContent(request, response,"/views/event-form.jsp", "/views/template/template.jsp");
        }

        if(editEventForm != null) {
            //modifier un évènement
            request.setAttribute("eventId", editEventForm);

            ServletUtils.forwardWithContent(request, response,"/views/event-form.jsp", "/views/template/template.jsp");
        }
    }
    /**
     * The method to send data in DB
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Récupération des données du formulaire
        String name = request.getParameter("eventName");
        String startDateStr = request.getParameter("startDateHour");
        String endDateStr = request.getParameter("endDateHour");
        String description = request.getParameter("description");
        String image = request.getParameter("image");
        String status = request.getParameter("status");

        //création de l'entité évènement
        Event event = new Event();

        event.setEventName(name);
        //conversion des dates en LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        try {
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);
            event.setBeginDateHour(startDate);
            event.setEndDateHour(endDate);
        }catch(DateTimeException error){
            throw new ServletException("Format de date invalide : " + error.getMessage());
        }
        event.setInfo(description);
        event.setPicture(image);
        //event.setActive(status);

        //transfert vers la DB



    }
}