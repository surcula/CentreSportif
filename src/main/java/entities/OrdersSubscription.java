package entities;

import javax.persistence.*;
import java.time.Instant;

@NamedQueries({
        @NamedQuery(
                name = "OrdersSubscription.countActiveByOrder",
                query = "SELECT COUNT(os) FROM OrdersSubscription os WHERE os.order = :o AND os.active = true"
        ),
        @NamedQuery(
                name = "OrdersSubscription.findActiveSubscriptionByOrder",
                query = "SELECT os.subscription FROM OrdersSubscription os WHERE os.order.id = :oid AND os.active = true"
        )
})

@Entity
@Table(name = "orders_subscriptions")
public class OrdersSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}