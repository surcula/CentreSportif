package services;


import interfaces.EventService;
import interfaces.ReservationService;
import Tools.Result;
import entities.Reservation;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import dto.EMF;
import dto.Page;
import org.apache.log4j.Logger;

/**
 * Implementation of the {@link ReservationService} interface
 * Provide concrete persistence operations for reservation entities
 * using the injected {@link javax.persistence.EntityManager}
 */
public class ReservationServiceImpl implements ReservationService {
    private EntityManager em;
    // Log4j
    private static Logger log = Logger.getLogger(ReservationServiceImpl.class);

    /**
     * Constructor for a reservationServiceImpl with the specified EntityManager
     * @param em the EntityManager to use for database operations
     */
    public ReservationServiceImpl(EntityManager em) {
        this.em = em;
    }

    /**
     * Method to create a new reservation
     * @param reservationCreateForm
     * @return
     */
    @Override
    public Result create(Reservation reservationCreateForm) {

        em.persist(reservationCreateForm);
        log.info("Reservation created");
        return Result.ok();
    }

    /**
     * Method to update an existing reservation
     * @param reservation
     * @return
     */
    @Override
    public Result update(Reservation reservation) {

        em.merge(reservation);
        log.info("Reservation updated");
        return Result.ok();
    }

    /**
     * Method to "delete" an existing reservation (put the status from active to inactive)
     * @param reservation
     * @return
     */
    @Override
    public Result softDelete(Reservation reservation) {

        em.merge(reservation);
        log.info("Reservation deleted");
        return Result.ok();
    }

    /**
     * Method to get a reservation with his ID
     * @param id
     * @return
     */
    @Override
    public Result<Reservation> getOneById(int id) {

        Reservation reservation = em.find(Reservation.class, id);
        if (reservation != null) {
            log.info("reservation found");
            return Result.ok(reservation);
        }else  {
            log.warn("reservation " + id + " not found");
            Map<String, String> errors = new HashMap<>();
            errors.put("notFound", "Aucune réservation trouvée avec l’ID " + id);
            return Result.fail(errors);
        }
    }

    /**
     * Method to get the list of all reservations
     * @param page page number starting at 1 (values <= 0 are treated as 1)
     * @param size number of reservations per page.
     *             If size <= 0, all active reservations are returned without pagination.
     * @return
     */
    public Result<Page<Reservation>> getAllReservations(int page, int size){
        em = EMF.getEM();
        try {
            if (page < 1) page = 1;
            if (size < 1) size = 10;

            int firstResult = (page - 1) * size;
            if(firstResult < 0) {
                System.err.println("ERREUR GRAVE : firstResult est négatif. Valeur calculée : " + firstResult + " (page = " + page + ", size = " + size + ")");
                firstResult = 0; //réinitialisation pour empêcher l'exception.
            }
            System.out.println(">> Pagination : page = " + page + ", size = " + size);

            List<Reservation> reservations = em.createNamedQuery("getAllReservations", Reservation.class)
                    .setFirstResult(firstResult)
                    .setMaxResults(size)
                    .getResultList();
            System.out.println(">> Reservations récupérées : " +reservations.size());
            for (Reservation r : reservations) {
                System.out.println("Reservation : " + r.getReservationName() + " / Active : " + r.isActive());
            }
            Long totalElements = em.createQuery("Select count(r) from Reservation r", Long.class)
                    .getSingleResult();
            Page<Reservation> pageData = Page.of(reservations, page, size, totalElements);
            return Result.ok(pageData);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errors = new HashMap<>();
            errors.put("Introuvable", "erreur lors du chargement des réservations" + e.getMessage());
            return Result.fail(errors);
        }
    }

    /**
     * Second method to get the list of all reservations
     * @param offset
     * @param size
     * @return
     */
    @Override
    public Result<List<Reservation>> getAllReservations2(int offset, int size) {
        List<Reservation> fields = em.createNamedQuery("getAllReservations", Reservation.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
        log.info("getAllReservations : " + fields.size());
        return Result.ok(fields);
    }

    /**
     * Method to get the list of all active reservations
     * @param offset page number starting at 1 (values <= 0 are treated as 1)
     * @param size number of elements per page.
     *             If size <= 0, all active reservations are returned without pagination.
     * @return result method
     */
    @Override
    public Result<List<Reservation>> getAllActiveReservations(int offset, int size) {
        List<Reservation> fields = em.createNamedQuery("getAllActiveReservations", Reservation.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
        log.info("getAllActiveReservations : " + fields.size());
        return Result.ok(fields);
    }

    /**
     * Method to count all the reservations
     * @return result method
     */
    @Override
    public Result<Long> countAllReservations() {
        Long countAllReservations = em.createNamedQuery("countAllReservations", Long.class).getSingleResult();
        log.info("getAllFields : " + countAllReservations);
        return Result.ok(countAllReservations);
    }

    /**
     * Method to count all the active reservations
     * @return result method
     */
    @Override
    public Result<Long> countAllActiveReservations() {
        Long countAllReservations = em.createNamedQuery("countAllActiveReservations", Long.class).getSingleResult();
        log.info("countAllActiveReservations : " + countAllReservations);
        return Result.ok(countAllReservations);
    }

}
