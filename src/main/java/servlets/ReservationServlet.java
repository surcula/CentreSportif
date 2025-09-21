package servlets;

import business.ServletUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ReservationServlet", value = "/reservation")
public class ReservationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reservationForm = request.getParameter("form");
        String editReservationForm = request.getParameter("editForm");

        if("true".equals(reservationForm)) {
            ServletUtils.forwardWithContent(request, response,"/views/reservation-form.jsp", "/views/template/template.jsp");
        }

        if(editReservationForm != null) {
            //modifier un évènement
            request.setAttribute("reservationId", editReservationForm);

            ServletUtils.forwardWithContent(request, response,"/views/reservation-form.jsp", "/views/template/template.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}