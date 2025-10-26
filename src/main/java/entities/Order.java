package entities;

import enums.OrderStatus;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "Order.findAll",
                query = "SELECT o FROM Order o ORDER BY o.dateOrder DESC"),
        @NamedQuery(name = "Order.countAll",
                query = "SELECT COUNT(o) FROM Order o"),
        @NamedQuery(name = "Order.findByUser",
                query = "SELECT o FROM Order o WHERE o.user.id = :uid ORDER BY o.dateOrder DESC"),
        @NamedQuery(name = "Order.countByUser",
                query = "SELECT COUNT(o) FROM Order o WHERE o.user.id = :uid"),

        // utile si tu veux recharger l’order avec ses liens
        @NamedQuery(name = "Order.findWithSubsById",
                query = "SELECT o FROM Order o " +
                        "LEFT JOIN FETCH o.ordersSubscriptions os " +
                        "LEFT JOIN FETCH os.subscription s " +
                        "LEFT JOIN FETCH o.ordersDiscounts od " +
                        "LEFT JOIN FETCH od.discount d " +
                        "WHERE o.id = :id")
})
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "id", nullable = false)
    private int id; // (si tu veux auto: @GeneratedValue(strategy = GenerationType.IDENTITY))

    @Column(name = "date_order", nullable = false)
    private Instant dateOrder;

    @Column(name = "total_price", nullable = false, precision = 15, scale = 2)
    private double totalPrice;

    @Column(name = "tva", nullable = false, precision = 15, scale = 2)
    private double tva;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    // ✅ on stocke le nom de l’enum
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private OrderStatus status;

    // ===== Relations =====
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdersSubscription> ordersSubscriptions = new ArrayList<>();

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdersDiscount> ordersDiscounts = new ArrayList<>();

    // ===== Getters/Setters =====
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Instant getDateOrder() { return dateOrder; }
    public void setDateOrder(Instant dateOrder) { this.dateOrder = dateOrder; }

    // 👉 helper pour JSTL <fmt:formatDate> (qui veut un java.util.Date)
    @Transient
    public Date getDateOrderAsDate() { return dateOrder != null ? Date.from(dateOrder) : null; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public double getTva() { return tva; }
    public void setTva(double tva) { this.tva = tva; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public List<OrdersSubscription> getOrdersSubscriptions() { return ordersSubscriptions; }
    public void setOrdersSubscriptions(List<OrdersSubscription> ordersSubscriptions) { this.ordersSubscriptions = ordersSubscriptions; }

    public List<OrdersDiscount> getOrdersDiscounts() { return ordersDiscounts; }
    public void setOrdersDiscounts(List<OrdersDiscount> ordersDiscounts) { this.ordersDiscounts = ordersDiscounts; }

    // helpers conviviaux
    public void addOrdersSubscription(OrdersSubscription os) {
        this.ordersSubscriptions.add(os);
        os.setOrder(this);
    }
    public void addOrdersDiscount(OrdersDiscount od) {
        this.ordersDiscounts.add(od);
        od.setOrder(this);
    }
}
