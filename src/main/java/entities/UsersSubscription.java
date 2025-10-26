package entities;

import javax.persistence.*;
import java.time.LocalDate;
@NamedQueries({
        @NamedQuery(
                name = "UsersSubscription.byUser",
                query = "SELECT us FROM UsersSubscription us " +
                        "WHERE us.user.id = :uid " +
                        "ORDER BY us.startDate DESC"
        ),
        @NamedQuery(
                name = "UsersSubscription.byUserAndSubscriptionActive",
                query = "SELECT us FROM UsersSubscription us " +
                        "WHERE us.user.id = :uid " +
                        "AND us.subscription.id = :sid " +
                        "AND us.active = true " +
                        "ORDER BY us.startDate DESC"
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