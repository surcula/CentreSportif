package entities;

import javax.persistence.*;
import java.time.LocalDate;
@NamedQueries({
        // Liste des US d’un user (ordre récent d’abord)
        @NamedQuery(
                name = "UsersSubscription.byUser",
                query = "SELECT us FROM UsersSubscription us " +
                        "WHERE us.user.id = :uid " +
                        "ORDER BY us.startDate DESC"
        ),
        // Trouver un US actif pour un user + sub à une date donnée
        @NamedQuery(
                name = "UsersSubscription.findActiveByUserAndSubscriptionAtDate",
                query = "SELECT us FROM UsersSubscription us " +
                        "WHERE us.user.id = :uid " +
                        "AND us.subscription.id = :sid " +
                        "AND us.active = true " +
                        "AND :today >= us.startDate " +
                        "AND :today <= us.endDate " +
                        "ORDER BY us.endDate DESC"
        ),
        // Version sans contrainte date (utile côté OrderService)
        @NamedQuery(
                name = "UsersSubscription.findActiveByUserAndSubscription",
                query = "SELECT us FROM UsersSubscription us " +
                        "WHERE us.user.id = :uid " +
                        "AND us.subscription.id = :sid " +
                        "AND us.active = true"
        ),
        // Charger US + user + subscription (pour créer l’order)
        @NamedQuery(
                name = "UsersSubscription.findByIdWithUserAndSubscription",
                query = "SELECT us FROM UsersSubscription us " +
                        "JOIN FETCH us.user u " +
                        "JOIN FETCH us.subscription s " +
                        "WHERE us.id = :id"
        ),
        // Récup du max(id) (si pas @GeneratedValue)
        @NamedQuery(
                name = "UsersSubscription.maxId",
                query = "SELECT COALESCE(MAX(us.id), 0) FROM UsersSubscription us"
        )
})

@Entity
@Table(name = "users_subscriptions")
public class UsersSubscription {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "quantity_max", nullable = false)
    private int quantityMax;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getQuantityMax() {
        return quantityMax;
    }

    public void setQuantityMax(int quantityMax) {
        this.quantityMax = quantityMax;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}