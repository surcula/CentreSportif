package entities;

import enums.ReservationStatus;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Named Query
 */
@NamedQueries({
        @NamedQuery(
                name = "getAllActiveReservations",
                query = "SELECT r FROM Reservation r WHERE r.active = true ORDER BY r.id asc"
        ),
        @NamedQuery(
                name = "getAllReservations",
                query = "SELECT r FROM Reservation r ORDER BY r.id desc"
        ),
        @NamedQuery(
                name = "countAllActiveReservations",
                query = "SELECT COUNT(r) FROM Reservation r WHERE r.active = true ORDER BY r.id asc"
        ),
        @NamedQuery(
                name = "countAllReservations",
                query = "SELECT COUNT(r) FROM Reservation r ORDER BY r.id asc"
        )
})

/**
 * Reservation entity to save the information of the reservation
 */
@Entity
@Table(name = "reservations")
public class Reservation {
    /**
     * Id of the reservation
     * automatically generated
     * @param id
     */
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    /**
     * The name of the reservation
     * @param reservation Name
     */
    @Column(name = "reservation_name", nullable = false)
    private String reservationName;
    /**
     * The starting date of the reservation
     * @param startDateReservation
     */
    @Column(name = "start_date_reservation", nullable = false)
    private LocalDateTime startDateReservation;
    /**
     * The end date of the reservation
     * @param endDateReservation
     */
    @Column(name = "end_date_reservation", nullable = false)
    private LocalDateTime endDateReservation;
    /**
     * the price of the reservation
     * @param price
     */
    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private double price;
    /**
     * the VAT of the reservation
     * @param TVA
     */
    @Column(name = "tva", nullable = false, precision = 15, scale = 2)
    private double tva;
    /**
     * The status of the reservation
     * @param statut
     */
    @Lob
    @Column(name = "statut", nullable = false)
    private ReservationStatus statut;
    /**
     * The name of the user found with id in the entity User
     * @param user
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * the name of the sportField based on the entity SportField
     * @param sportField
     */
    //@ManyToOne
    //@JoinColumn(name = "sports_field_id")
    @Transient
    private SportField sportField;
    /**
     * The status of the sportField based on the entity SportField
     * @param active
     */
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    /**
     * The method to get the id of the reservation
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * The method to set the id of the reservation
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * The method to get the name of the reservation
     * @return reservationName
     */
    public String getReservationName() {
        return reservationName;
    }

    /**
     * The method to set the name of the reservation
     * @param reservationName
     */
    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }

    /**
     * The method to get the starting date of the reservation
     * @return startDateReservation
     */
    public LocalDateTime getStartDateReservation() {
        return startDateReservation;
    }

    /**
     * the method to set the starting date of the reservation
     * @param startDateReservation
     */
    public void setStartDateReservation(LocalDateTime startDateReservation) {
        this.startDateReservation = startDateReservation;
    }

    /**
     * the method to get the end date of the reservation
     * @return endDateReservation
     */
    public LocalDateTime getEndDateReservation() {
        return endDateReservation;
    }

    /**
     * The method to set the end date of the reservation
     * @param endDateReservation
     */
    public void setEndDateReservation(LocalDateTime endDateReservation) {
        this.endDateReservation = endDateReservation;
    }

    /**
     * The method to get the price of the reservation
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * The method to set the price of the reservation
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * The method to set the VAT of the reservation
     * @return tva
     */
    public double getTva() {
        return tva;
    }

    /**
     * The method to set the VAT of the reservation
     * @param tva
     */
    public void setTva(double tva) {
        this.tva = tva;
    }

    /**
     * The method to get the status of the reservation
     * @return statut
     */
    public ReservationStatus getStatut() {
        return statut;
    }

    /**
     * The method to set the status of the reservation
     * @param statut
     */
    public void setStatut(ReservationStatus statut) {
        this.statut = statut;
    }

    /**
     * The method tu get the User of the reservation based on the id founded in the entity User
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * the method to set the user of the reservation
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * The method to get the sportField of the reservation based on the entity SportField
     * @return sportField
     */
    public SportField getSportsField() {
        return sportField;
    }

    /**
     * The method to set the SportField of the reservation
     * @param sportField
     */
    public void setSportsField(SportField sportField) {
        this.sportField = sportField;
    }

    /**
     * The method to get if the reservation is active or no
     * @return active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * the method to set if the reservation is active or no
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

}