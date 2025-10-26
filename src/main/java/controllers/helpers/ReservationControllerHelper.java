package controllers.helpers;

import business.ServletUtils;
import entities.Reservation;
import static constants.Rooting.*;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class ReservationControllerHelper {
    /**
     * Forwards to the reservation form JSP in creation mode,
     * attaching the list of available reservation for selection
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public static void handleFormDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtils.forwardWithContent(request, response, RESERVATION_FORM_JSP, TEMPLATE);
    }

    /**
     * Forwards to the reservation form JSP in edit mode,
     * attaching the list of available reservation for selection
     * @param request
     * @param response
     * @param reservation
     * @throws ServletException
     * @throws IOException
     */
    public static void handleEditForm(HttpServletRequest request, HttpServletResponse response, Reservation reservation) throws ServletException, IOException {
        request.setAttribute("reservation", reservation);
        request.setAttribute("reservationName", reservation.getReservationName());
        request.setAttribute("reservationStartDate", reservation.getStartDateReservation());
        request.setAttribute("reservationEndDateHour", reservation.getEndDateReservation());
        request.setAttribute("reservationPrice", reservation.getPrice());
        request.setAttribute("reservationTVA", reservation.getTva());
        request.setAttribute("reservationStatus", reservation.getStatut());
        request.setAttribute("reservationUserId", reservation.getUser().getId());
        request.setAttribute("reservationSportFieldId", reservation.getSportsField().getId());
        request.setAttribute("reservationActive", reservation.isActive());
        ServletUtils.forwardWithContent(request, response, RESERVATION_FORM_JSP, TEMPLATE);
    }

    /**
     * Forwards to the reservation list
     * @param request
     * @param response
     * @param reservations
     * @throws ServletException
     * @throws IOException
     */
    public static void handleList(HttpServletRequest request, HttpServletResponse response, List<Reservation> reservations) throws ServletException, IOException {
        request.setAttribute("reservations", reservations);
        ServletUtils.forwardWithContent(request, response, RESERVATION_JSP, TEMPLATE);
        System.out.println(">>>>>>>>>> \n\n===========================================");
        System.out.println(">>>>>>>>>> handlelist est appelé");
        System.out.println("===========================\n");
    }
}
