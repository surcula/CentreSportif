package entities;

import javax.persistence.*;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "subscription_name", nullable = false)
    private String subscriptionName;

    @Column(name = "token", nullable = false)
    private int token;

    @ManyToOne
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}