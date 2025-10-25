package interfaces;

import entities.Reservation;
import java.util.List;
import Tools.Result;
import dto.Page;


public interface ReservationService {
    /**
     * Create a new reservation
     * @param reservationCreateForm
     */

    Tools.Result create(Reservation reservationCreateForm) ;

    /**
     * Update an existing reservation
     * @param reservation
     */
    Tools.Result update(Reservation reservation) ;

    /**
     * soft-delete a reservation by setting its isActive flag to false
     * @param reservation
     */
    Tools.Result softDelete(Reservation reservation) ;

    /**
     * retrieves a reservation by its id
     * @param id
     * @return Reservation
     */
    Tools.Result<Reservation> getOneById(int id);

    /**
     * Retrieves all active Reservations with optional pagination.
     *
     * @param offset page number starting at 1 (values <= 0 are treated as 1)
     * @param size number of elements per page.
     *             If size <= 0, all active reservations are returned without pagination.
     * @return Result containing a list of active Reservations (never null).
     */
    Tools.Result<List<Reservation>> getAllActiveReservations(int offset, int size) ;

    /**
     * Retrieves all Reservations with optional pagination.
     *
     * @param offset page number starting at 1 (values <= 0 are treated as 1)
     * @param size number of elements per page.
     *             If size <= 0, all active reservations are returned without pagination.
     * @return Result containing a Page of Reservationss (never null).
     */
    Tools.Result<Page<Reservation>> getAllReservations(int offset, int size);

    /**
     * Search all reservations
     * @param offset
     * @param size
     * @return Result containing a list of Reservationss (never null).
     */
    Tools.Result<List<Reservation>> getAllReservations2(int offset, int size) ;

    /**
     * count all active reservations
     * @return a number
     */
    Tools.Result<Long> countAllActiveReservations() ;

    /**
     * count all reservations
     * @return a number
     */
    Tools.Result<Long> countAllReservations() ;

}
