package services;

import Tools.Result;
import business.OrderBusiness;
import entities.Discount;
import entities.Order;
import entities.OrdersDiscount;
import entities.OrdersSubscription;
import entities.Subscription;
import entities.UsersSubscription;
import enums.OrderStatus;
import interfaces.OrderService;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

public class OrderServiceImpl implements OrderService {

    private final EntityManager em;
    private static final Logger log = Logger.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(EntityManager em) {
        this.em = em;
    }

    /* ================= CRUD & LIST ================= */

    @Override
    public Result<Order> create(Order order) {
        em.persist(order);
        log.info("Order.create id=" + order.getId());
        return Result.ok(order);
    }

    @Override
    public Result<Order> findOne(int id) {
        Order o = em.find(Order.class, id);
        if (o == null) return Result.fail(Collections.singletonMap("notFound","Order "+id+" introuvable"));
        return Result.ok(o);
    }

    @Override
    public Result<List<Order>> findAll() {
        List<Order> list = em.createQuery("SELECT o FROM Order o ORDER BY o.dateOrder DESC", Order.class)
                .getResultList();
        log.info("Order.findAll size=" + list.size());
        return Result.ok(list);
    }

    @Override
    public Result<List<Order>> getAllOrders(int offset, int limit) {
        List<Order> list = em.createQuery("SELECT o FROM Order o ORDER BY o.dateOrder DESC", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        log.info("Order.getAllOrders size=" + list.size() + " offset=" + offset + " limit=" + limit);
        return Result.ok(list);
    }

    @Override
    public Result<Long> countAllOrders() {
        Long c = em.createQuery("SELECT COUNT(o) FROM Order o", Long.class).getSingleResult();
        log.info("Order.countAllOrders = " + c);
        return Result.ok(c);
    }

    @Override
    public Result<List<Order>> findByUser(int userId, int offset, int limit) {
        List<Order> list = em.createQuery(
                        "SELECT o FROM Order o WHERE o.user.id = :uid ORDER BY o.dateOrder DESC", Order.class)
                .setParameter("uid", userId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        log.info("Order.findByUser size=" + list.size() + " userId=" + userId);
        return Result.ok(list);
    }

    @Override
    public Result<Long> countByUser(int userId) {
        Long c = em.createQuery("SELECT COUNT(o) FROM Order o WHERE o.user.id = :uid", Long.class)
                .setParameter("uid", userId)
                .getSingleResult();
        log.info("Order.countByUser = " + c + " userId=" + userId);
        return Result.ok(c);
    }

    /* ================= BUSINESS ACTIONS ================= */

    @Override
    public Result<Order> applyDiscount(int orderId, int discountId) {
        Order o = em.find(Order.class, orderId);
        if (o == null) return Result.fail(Collections.singletonMap("notFound","Order "+orderId+" introuvable"));

        Discount d = em.find(Discount.class, discountId);
        if (d == null) return Result.fail(Collections.singletonMap("notFound","Discount "+discountId+" introuvable"));

        // check actif (simple)
        if (!d.isActive()) return Result.fail(Collections.singletonMap("discount","La remise n'est pas active"));

        OrdersDiscount od = new OrdersDiscount();
        od.setOrder(o);
        od.setDiscount(d);
        od.setActive(true);
        od.setCreatedAt(Instant.now());
        od.setUpdatedAt(Instant.now());
        em.persist(od);

        // recalcule total via Business
        List<OrdersDiscount> ods = em.createQuery(
                        "SELECT x FROM OrdersDiscount x WHERE x.order.id = :oid AND x.active = true", OrdersDiscount.class)
                .setParameter("oid", o.getId())
                .getResultList();

        Result<Double> total = OrderBusiness.computeDiscountedTotal(o.getTotalPrice(), ods);
        if (!total.isSuccess()) return Result.fail(total.getErrors());
        o.setTotalPrice(total.getData().doubleValue());
        em.merge(o);

        log.info("Order.applyDiscount ok orderId=" + orderId + " discountId=" + discountId);
        return Result.ok(o);
    }

    @Override
    public Result<Order> linkUserSubscription(int orderId, int subscriptionId) {
        Order o = em.find(Order.class, orderId);
        if (o == null) return Result.fail(Collections.singletonMap("notFound","Order "+orderId+" introuvable"));

        Subscription sub = em.find(Subscription.class, subscriptionId);
        if (sub == null) return Result.fail(Collections.singletonMap("notFound","Subscription "+subscriptionId+" introuvable"));

        OrdersSubscription os = new OrdersSubscription();
        os.setOrder(o);
        os.setSubscription(sub);
        os.setActive(true);
        os.setCreatedAt(Instant.now());
        os.setUpdatedAt(Instant.now());
        em.persist(os);

        log.info("Order.linkUserSubscription ok orderId=" + orderId + " subId=" + subscriptionId);
        return Result.ok(o);
    }

    @Override
    public Result<Order> confirmBySubscription(int orderId) {
        Order o = em.find(Order.class, orderId);
        if (o == null) return Result.fail(Collections.singletonMap("notFound","Order "+orderId+" introuvable"));

        Subscription sub = fetchLinkedSubscription(orderId);
        if (sub == null) return Result.fail(Collections.singletonMap("subscription","Pas d'abonnement lié à la commande"));

        UsersSubscription us = fetchUsersSubscriptionFor(o.getUser().getId(), sub.getId());
        if (us == null) return Result.fail(Collections.singletonMap("subscription","Aucun UsersSubscription pour cet abonnement"));

        LocalDate today = LocalDate.now();
        boolean dateOk = !today.isBefore(us.getStartDate()) && !today.isAfter(us.getEndDate());
        if (!us.isActive() || !dateOk) return Result.fail(Collections.singletonMap("subscription","Abonnement inactif ou expiré"));
        if (us.getQuantityMax() <= 0) return Result.fail(Collections.singletonMap("subscription","Solde insuffisant"));

        us.setQuantityMax(us.getQuantityMax() - 1);
        em.merge(us);

        o.setStatus(OrderStatus.CONFIRMED);
        em.merge(o);

        log.info("Order.confirmBySubscription ok orderId=" + orderId);
        return Result.ok(o);
    }

    @Override
    public Result<Order> confirmByOnlinePayment(int orderId) {
        Order o = em.find(Order.class, orderId);
        if (o == null) return Result.fail(Collections.singletonMap("notFound","Order "+orderId+" introuvable"));

        o.setStatus(OrderStatus.CONFIRMED);
        em.merge(o);

        log.info("Order.confirmByOnlinePayment ok orderId=" + orderId);
        return Result.ok(o);
    }

    @Override
    public Result<Order> cancel(int orderId) {
        Order o = em.find(Order.class, orderId);
        if (o == null) return Result.fail(Collections.singletonMap("notFound","Order "+orderId+" introuvable"));

        if (o.getStatus() == OrderStatus.CONFIRMED) {
            Subscription sub = fetchLinkedSubscription(orderId);
            if (sub != null) {
                UsersSubscription us = fetchUsersSubscriptionFor(o.getUser().getId(), sub.getId());
                if (us != null) {
                    us.setQuantityMax(us.getQuantityMax() + 1); // recrédit
                    em.merge(us);
                }
            } else {
                // TODO: remboursement PSP à implémenter
                log.info("Order.cancel: remboursement carte à faire (orderId="+orderId+")");
            }
        }

        o.setStatus(OrderStatus.CANCELLED);
        em.merge(o);

        log.info("Order.cancel ok orderId=" + orderId);
        return Result.ok(o);
    }

    @Override
    public Result<Order> cancelUnpaid(int orderId) {
        Order o = em.find(Order.class, orderId);
        if (o == null) return Result.fail(Collections.singletonMap("notFound","Order "+orderId+" introuvable"));
        if (o.getStatus() != OrderStatus.ONHOLD)
            return Result.fail(Collections.singletonMap("status","La commande n'est pas ONHOLD"));

        // supprimer liens
        em.createQuery("DELETE FROM OrdersDiscount od WHERE od.order.id = :oid")
                .setParameter("oid", orderId).executeUpdate();
        em.createQuery("DELETE FROM OrdersSubscription os WHERE os.order.id = :oid")
                .setParameter("oid", orderId).executeUpdate();

        em.remove(o);
        log.info("Order.cancelUnpaid ok orderId=" + orderId);
        return Result.ok(o);
    }

    /* ================= HELPERS ================= */

    private Subscription fetchLinkedSubscription(int orderId) {
        try {
            TypedQuery<Subscription> q = em.createQuery(
                    "SELECT os.subscription FROM OrdersSubscription os WHERE os.order.id = :oid AND os.active = true",
                    Subscription.class);
            q.setParameter("oid", orderId);
            q.setMaxResults(1);
            return q.getSingleResult();
        } catch (NoResultException e) { return null; }
    }

    private UsersSubscription fetchUsersSubscriptionFor(int userId, int subscriptionId) {
        try {
            TypedQuery<UsersSubscription> q = em.createQuery(
                    "SELECT us FROM UsersSubscription us " +
                            "WHERE us.user.id = :uid AND us.subscription.id = :sid AND us.active = true",
                    UsersSubscription.class);
            q.setParameter("uid", userId);
            q.setParameter("sid", subscriptionId);
            q.setMaxResults(1);
            return q.getSingleResult();
        } catch (NoResultException e) { return null; }
    }
}
