package servlets;

import business.ServletUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "EventServlet", value = "/event")
public class EventServlet extends HttpServlet {
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}