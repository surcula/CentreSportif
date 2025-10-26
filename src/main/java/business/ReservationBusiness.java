package business;

import javax.persistence.EntityManager;

import entities.Reservation;
import enums.ReservationStatus;
import services.ReservationServiceImpl;
import dto.Page;
import Tools.Result;
import enums.Scope;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.*;
import java.util.*;

/**
 * Class to handle reservation-related validation
 * and initialisation before database operations.
 * So this class validate the input from the reservation creation form and convert it into a valid {@link Reservation}
 */
public class ReservationBusiness {
    private EntityManager em;
    private ReservationServiceImpl reservationService;

    /**
     * Constructor
     * @param reservationService
     * @param em
     */
    public ReservationBusiness(ReservationServiceImpl reservationService, EntityManager em) {
        this.reservationService = reservationService;
        this.em = em;
    }
    //Log4J
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ReservationBusiness.class);
    /**
     * Initialisation and validation of the data submitted via the Reservation creation Form
     * @param name the name of the reservation
     * @param startDateStr the start date and time in format "yyyy-MM-dd'T'HH:mm"
     * @param endDateStr the end date and time in format "yyyy-MM-dd'T'HH:mm"
     * @param status the status of the reservation, 1 for "En cours" and 0 for "Terminé"
     * @param isActive the information about the reservation if the reservation is active or not
     * @return a {@link Result} which contains a valid {@link Reservation} instance or an errors
     */
    public static Result<Reservation> initCreateForm(String name, String startDateStr, String endDateStr,
                                               String status, String isActive ){
        //List<String> errors = new ArrayList<>();
        Map<String, String> errors = new HashMap<>();
        Reservation reservation = new Reservation();

        //validation du nom
        if(name == null || name.trim().isEmpty()) {
            errors.put("name", "Le nom est obligatoire");
        } else {
            reservation.setReservationName(name.trim());
        }
        //Validation et conversion des dates et heures

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        //Date de début
        if(startDateStr == null || startDateStr.trim().isEmpty()) {
            errors.put("startDateHour", "Le date et heure de début sont obligatoires");
        }else {
            try {
                //CORRECTION
                startDate = LocalDateTime.parse(startDateStr, formatter);
                reservation.setStartDateReservation(startDate);
            } catch (DateTimeParseException e) {
                errors.put("startDateHour", "Format de date invalide (doit être yyyy-MM-ddTHH:mm).");
            }
        }
        //Date de fin
        if(endDateStr == null || endDateStr.trim().isEmpty()) {
            errors.put("endDateHour", "Le date et heure de fin sont obligatoires");
        }else {
            try {
                //CORRECTION
                endDate = LocalDateTime.parse(endDateStr, formatter);
                reservation.setEndDateReservation(endDate);
            } catch (DateTimeParseException e) {
                errors.put("endDateHour", "Format de date invalide (doit être yyyy-MM-ddTHH:mm).");
            }
        }
        //validation que la date de début est antérieure ou égale à la date de fin
        if(startDate != null && endDate != null && startDate.isAfter(endDate)) {
            errors.put("endDateHour", "La date de fin ne peut pas être antérieure à la date de début.");
        }
        //Statut
        if (status == null || status.trim().isEmpty()) {
            errors.put("status", "Le statut est obligatoire");
        } else {
            try{
                ReservationStatus reservationStatus = ReservationStatus.valueOf(status.toUpperCase());
                reservation.setStatut(reservationStatus);
            } catch (IllegalArgumentException e) {
                errors.put("status", "Statut invalide. Veuillez choisir parmi les options valides.");
            }
        }
        //Actif/inactif ?
        boolean active = "1".equals(status);
        reservation.setActive(active);
        if(errors.isEmpty()) {
            return Result.ok(reservation);
        } else {
            return Result.fail(errors);
        }
    }

    /**
     * Method to get all the reservations page
     * @param page
     * @param size
     * @param scope
     * @return Result
     */
    public Result<Page<Reservation>> getAllReservationsPaged(int page, int size, Scope scope) {

        int pageNumber = Math.max(1, page);
        int pageSize = Math.max(1, size);
        int offset = (pageNumber - 1) * pageSize;

        try {
            List<Reservation> reservations;

            if(scope == Scope.ALL) {
                System.out.println(">>>>>> [ADMIN] Récupération de toutes les réservations (actives + inactives) ");
                reservations = em.createNamedQuery("getAllReservations", Reservation.class)
                        .setFirstResult(offset)
                        .setMaxResults(pageSize)
                        .getResultList();
            }
            else {
                System.out.println(">>>>>>> [USER] Récupération uniquement des réservations actives");
                reservations = em.createNamedQuery("getAllActiveReservations", Reservation.class)
                        .setFirstResult(offset)
                        .setMaxResults(pageSize)
                        .getResultList();
            }
            // Log de debug
            System.out.println((">>>>> Nombre de réservations récupérées : " + reservations.size()));
            //Total d'éléments pour la pagination
            Long totalElements = (scope == Scope.ALL)
                    ? em.createQuery("SELECT COUNT(r) FROM Reservation r", Long.class).getSingleResult()
                    : em.createQuery("SELECT COUNT(r) FROM Reservation r WHERE r.active = true", Long.class).getSingleResult();
            //Création de la page
            Page<Reservation> pageData = Page.of(reservations, pageNumber, pageSize, totalElements);

            return Result.ok(pageData);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errors = new HashMap<>();
            errors.put("Erreur","Erreur lors de la récupération des réservations : " + e.getMessage());
            return Result.fail(errors);
        }
    }
}
