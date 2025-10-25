package servlets;

import business.ServletUtils;
import business.ReservationBusiness;
import controllers.helpers.ReservationControllerHelper;
import dto.EMF;//ok
import dto.Page;//ok
import entities.Reservation;//OK
import enums.Scope;
import java.util.HashMap;
import interfaces.ReservationService;//ok
import services.ReservationServiceImpl;
import static constants.Rooting.*;
import Tools.Result;//ok
import javax.persistence.EntityManager;//ok
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import Tools.ParamUtils;

/**
 * The class to view the JSP form page and to send the data of the form in DB
 * @author Franz
 */
@MultipartConfig
@WebServlet(name = "ReservationServlet", value = "/reservation")
public class ReservationServlet extends HttpServlet {
    // Log4j
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ReservationServlet.class);

    /**
     * Initialisation of the servlet with a message to be sur ethe servlet is activated
     * @param config
     * @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log.info(">>> [ReservationServlet] Initialisation de la servlet");
    }
    private EntityManager em;
    private ReservationService reservationService;
    /**
     * The method to view the form and the list of reservation
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String reservationForm = request.getParameter("form");
        String editReservationForm = request.getParameter("editForm");
        String role = (session!= null && session.getAttribute("role") != null)
                ? session.getAttribute("role").toString()
                : null;
        boolean fullAccess = ServletUtils.isFullAuthorized(role);

        //FORMULAIRE DE CREATION
        if(reservationForm != null) {
            if(!fullAccess){
                ServletUtils.redirectNoAuthorized(request, response);
                return;
            }
            ReservationControllerHelper.handleFormDisplay(request, response);
            return;
        }
        //FORMULAIRE D'EDITION
        if(editReservationForm != null) {
            if(!fullAccess){
                ServletUtils.redirectNoAuthorized(request, response);
                return;
            }
            EntityManager em = EMF.getEM();
            try {
                ReservationService reservationService = new ReservationServiceImpl(em);
                Result<Reservation> result = reservationService.getOneById(Integer.parseInt(editReservationForm));
                if(result.isSuccess()){
                    ReservationControllerHelper.handleEditForm(request, response, result.getData());
                } else {
                    ServletUtils.forwardWithErrors(request, response, result.getErrors(), RESERVATION_FORM_JSP, TEMPLATE);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                ServletUtils.forwardWithError(request, response, e.getMessage(), RESERVATION_JSP, TEMPLATE);
            } finally {
                em.close();
            }
            return;
        }

        //LISTE PAGINEE
        EntityManager em = null;
        try {
            em = EMF.getEM();
            ReservationServiceImpl reservationService = new ReservationServiceImpl(em);
            ReservationBusiness reservationBusiness = new ReservationBusiness(reservationService, em);

            Result<Integer> pageRes = ParamUtils.stringToInteger(request.getParameter("page"));
            int page = pageRes.isSuccess() ? Math.max(1, pageRes.getData()) : 1;

            Result<Integer> sizeRes = ParamUtils.stringToInteger(request.getParameter("size"));
            int size = sizeRes.isSuccess() ? Math.min(10, Math.max(1, sizeRes.getData())) : 10;


            Result<Page<Reservation>> result = fullAccess
                    ? reservationBusiness.getAllReservationsPaged(page, size, Scope.ALL)
                    : reservationBusiness.getAllReservationsPaged(page, size, Scope.ACTIVE);

            if(result.isSuccess()) {
                Page<Reservation> p = result.getData();
                request.setAttribute("page", p.getPage());
                request.setAttribute("size", p.getSize());
                request.setAttribute("totalPages", p.getTotalPages());
                request.setAttribute("totalElements", p.getTotalElements());
                request.setAttribute("fullAccess", fullAccess);
                ReservationControllerHelper.handleList(request, response, p.getContent());
                return;
            } else {
                ServletUtils.forwardWithErrors(request, response, result.getErrors(), RESERVATION_JSP, TEMPLATE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Erreur doGet reservations", e);
            ServletUtils.forwardWithError(request, response, e.getMessage(), RESERVATION_JSP, TEMPLATE);
        } finally {
            if(em !=null) {
                em.close();
            }

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
        EntityManager em= null;

        try{
            em = EMF.getEM();
            ReservationServiceImpl reservationService = new ReservationServiceImpl(em);
            String reservationIdStr = request.getParameter("reservationId");
            boolean isEdit = (reservationIdStr != null && !reservationIdStr.isEmpty());
            String action = request.getParameter("action");

            log.info("doPost invoked - reservationIdStr =" + request.getParameter("reservationId"));
            //Changer le statut de Terminé vers en cours et supprimer mettre le statut de En cours vers terminé

            if (isEdit && action != null && !action.isEmpty()) {
                int reservationId = Integer.parseInt(reservationIdStr);
                Result<Reservation> existingReservationRest = reservationService.getOneById(reservationId);

                if (!existingReservationRest.isSuccess()) {
                    ServletUtils.redirectWithMessage(request, response, "Evènement introuvable pour la mise à jour du statut.", RESERVATION_JSP, TEMPLATE);
                    return;
                }
                Reservation reservationToUpdate = existingReservationRest.getData();

                em.getTransaction().begin();

                String successMessage = "";
                if ("delete".equals(action)) {
                    //utilisation du softDelete du reservationServiceImpl
                    reservationToUpdate.setActive(false);
                    reservationService.softDelete(reservationToUpdate);
                    successMessage = "Reservation marqué comme terminé avec succès.";
                } else if ("activer".equals(action)) {
                    //utilisation du update du reservationServiceImpl
                    reservationToUpdate.setActive(true);
                    reservationService.update(reservationToUpdate);
                    successMessage = "Reservation remise En cours avec succès.";
                } else {
                    //opération inadéquate
                    em.getTransaction().rollback();
                    ServletUtils.redirectWithMessage(request, response, "Action de statut non reconnue.", RESERVATION_JSP, TEMPLATE);
                    return;
                }
                //Validation des changements
                em.getTransaction().commit();
                //Succès => page reservation
                ServletUtils.redirectWithMessage(request, response, successMessage, "/reservation", TEMPLATE);
                return;
            }

            } catch (Exception e) {
                log.error("ID de réservation invalide.", e);
                if (em!= null && em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                ServletUtils.redirectWithMessage(request, response, "Erreur lors de la mise à jour du statut : " + e.getMessage(), RESERVATION_JSP, TEMPLATE);
            } finally {
                if (em != null) em.close();
            }
        }


}