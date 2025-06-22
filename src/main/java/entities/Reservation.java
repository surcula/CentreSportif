package entities;

import enums.ReservationStatus;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "reservation_name", nullable = false)
    private String reservationName;

    @Column(name = "start_date_reservation", nullable = false)
    private Instant startDateReservation;

    @Column(name = "end_date_reservation", nullable = false)
    private Instant endDateReservation;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private double price;

    @Column(name = "tva", nullable = false, precision = 15, scale = 2)
    private double tva;

    @Lob
    @Column(name = "statut", nullable = false)
    private ReservationStatus statut;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "sports_field_id")
    private SportField sportField;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReservationName() {
        return reservationName;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }

    public Instant getStartDateReservation() {
        return startDateReservation;
    }

    public void setStartDateReservation(Instant startDateReservation) {
        this.startDateReservation = startDateReservation;
    }

    public Instant getEndDateReservation() {
        return endDateReservation;
    }

    public void setEndDateReservation(Instant endDateReservation) {
        this.endDateReservation = endDateReservation;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTva() {
        return tva;
    }

    public void setTva(double tva) {
        this.tva = tva;
    }

    public ReservationStatus getStatut() {
        return statut;
    }

    public void setStatut(ReservationStatus statut) {
        this.statut = statut;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SportField getSportsField() {
        return sportField;
    }

    public void setSportsField(SportField sportField) {
        this.sportField = sportField;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}