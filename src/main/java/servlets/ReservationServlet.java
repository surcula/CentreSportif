package servlets;

import business.ServletUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

/**
 * The class to view the JSP form page and to send the data of the form in DB
 * @author Franz
 */
@WebServlet(name = "ReservationServlet", value = "/reservation")
public class ReservationServlet extends HttpServlet {
    /**
     * The method to view the form
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
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

    /**
     * The method to send the data in DB
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}