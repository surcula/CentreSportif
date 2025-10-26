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
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {

    private final EntityManager em;
    private static final Logger log = Logger.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(EntityManager em) { this.em = em; }

    /* ================= PRICING HELPERS ================= */

    // Prix effectif d’un sport à une date (SQL natif only)
    private Double findSportPriceEffective(int sportId, LocalDate when) {
        if (when == null) when = LocalDate.now();

        // 1) prix courant (table: sports)
        Double current;
        try {
            Object r0 = em.createNativeQuery(
                            "SELECT price FROM sports WHERE id = ? LIMIT 1")
                    .setParameter(1, sportId)
                    .getSingleResult();
            current = (r0 != null) ? ((Number) r0).doubleValue() : null;
        } catch (Exception e) {
            log.error("current sport price fetch failed", e);
            current = null;
        }
        if (current == null) {
            return 0d; // safety
        }

        // 2) tentative d’historique (table: historicals_sports_prices)
        try {
            Object r = em.createNativeQuery(
                            "SELECT h.price " +
                                    "FROM historicals_sports_prices h " +
                                    "WHERE h.sport_id = ? AND h.start_date <= ? " +
                                    "ORDER BY h.start_date DESC " +
                                    "LIMIT 1")
                    .setParameter(1, sportId)
                    .setParameter(2, Date.valueOf(when))
                    .getSingleResult();

            if (r != null) {
                try { return ((Number) r).doubleValue(); } catch (Exception ignore) {}
            }
        } catch (Exception ignore) {
            // pas d’historique / autre schéma → on garde le prix courant
        }

        return current;
    }

    // Devis sport+qty+date (rien persisté) → à appeler côté servlet avant create(order)
    public Result<Map<String, Double>> quoteFromSport(int sportId, int quantity, LocalDate forDate) {
        try {
            if (quantity <= 0) return Result.fail(Collections.singletonMap("quantity", "Quantité invalide"));
            if (forDate == null) forDate = LocalDate.now();

            Double unit = findSportPriceEffective(sportId, forDate);
            if (unit == null) return Result.fail(Collections.singletonMap("price", "Prix introuvable pour le sport"));

            double total = unit * quantity;

            Map<String, Double> data = new HashMap<>();
            data.put("unit", unit);
            data.put("total", total);
            return Result.ok(data);
        } catch (Exception e) {
            log.error("quoteFromSport failed", e);
            return Result.fail(Collections.singletonMap("error", "Erreur devis: " + e.getMessage()));
        }
    }

    /* ================= CRUD & LIST ================= */

    @Override
    public Result<Order> create(Order order) {
        // Si le contrôleur n’a pas fixé totalPrice via quoteFromSport(...), on persiste tel quel.
        if (order.getTotalPrice() <= 0) {
            log.warn("Order.create: totalPrice=0 — appelle quoteFromSport(...) côté servlet pour fixer un prix base avant create().");
        }
        em.persist(order);
        log.info("Order.create id=" + order.getId());
        return Result.ok(order);
    }

    @Override
    public Result<Order> findOne(int id) {
        Order o = em.find(Order.class, id);
        if (o == null) return Result.fail(Collections.singletonMap("notFound", "Order " + id + " introuvable"));
        return Result.ok(o);
    }

    @Override
    public Result<List<Order>> findAll() {
        List<Order> list = em.createQuery("SELECT o FROM Order o ORDER BY o.dateOrder DESC", Order.class).getResultList();
        log.info("Order.findAll size=" + list.size());
        return Result.ok(list);
    }

    @Override
    public Result<List<Order>> getAllOrders(int offset, int limit) {
        List<Order> list = em.createQuery("SELECT o FROM Order o ORDER BY o.dateOrder DESC", Order.class)
                .setFirstResult(offset).setMaxResults(limit).getResultList();
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
        if (o == null) return Result.fail(Collections.singletonMap("notFound", "Order " + orderId + " introuvable"));

        Discount d = em.find(Discount.class, discountId);
        if (d == null) return Result.fail(Collections.singletonMap("notFound", "Discount " + discountId + " introuvable"));
        if (!d.isActive()) return Result.fail(Collections.singletonMap("discount", "La remise n'est pas active"));

        OrdersDiscount od = new OrdersDiscount();
        od.setOrder(o);
        od.setDiscount(d);
        od.setActive(true);
        od.setCreatedAt(Instant.now());
        od.setUpdatedAt(Instant.now());
        em.persist(od);

        List<OrdersDiscount> ods = em.createQuery(
                        "SELECT x FROM OrdersDiscount x WHERE x.order.id = :oid AND x.active = true", OrdersDiscount.class)
                .setParameter("oid", o.getId())
                .getResultList();

        Result<Double> total = OrderBusiness.computeDiscountedTotal(o.getTotalPrice(), ods);
        if (!total.isSuccess()) return Result.fail(total.getErrors());
        o.setTotalPrice(total.getData());
        em.merge(o);

        log.info("Order.applyDiscount ok orderId=" + orderId + " discountId=" + discountId);
        return Result.ok(o);
    }

    @Override
    public Result<Order> applyAutoDiscounts(Integer orderId, boolean clubEligible) {
        try {
            Order order = em.find(Order.class, orderId);
            if (order == null) {
                return Result.fail(Collections.singletonMap("notFound", "Commande introuvable ou inactive."));
            }

            Integer clubId  = findDiscountIdByName("Club -10%");
            Integer multiId = findDiscountIdByName("Multi-Abonnements -5%");

            if (clubEligible && clubId != null) {
                Result<Order> r1 = this.applyDiscount(orderId, clubId);
                if (!r1.isSuccess()) return r1;
                order = r1.getData();
            }

            Long subsCount = em.createQuery(
                    "SELECT COUNT(os) FROM OrdersSubscription os WHERE os.order = :o AND os.active = true",
                    Long.class
            ).setParameter("o", order).getSingleResult();

            if (subsCount != null && subsCount >= 2 && multiId != null) {
                Result<Order> r2 = this.applyDiscount(orderId, multiId);
                if (!r2.isSuccess()) return r2;
                order = r2.getData();
            }

            return Result.ok(order);
        } catch (Exception e) {
            return Result.fail(Collections.singletonMap("error", "applyAutoDiscounts a échoué: " + e.getMessage()));
        }
    }

    private Integer findDiscountIdByName(String name) {
        try {
            return em.createQuery(
                    "SELECT d.id FROM Discount d WHERE d.discountName = :n AND d.active = true",
                    Integer.class
            ).setParameter("n", name).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) { return null; }
        catch (Exception e) { return null; }
    }

    @Override
    public Result<Order> linkUserSubscription(int orderId, int subscriptionId) {
        Order o = em.find(Order.class, orderId);
        if (o == null) return Result.fail(Collections.singletonMap("notFound", "Order " + orderId + " introuvable"));

        Subscription sub = em.find(Subscription.class, subscriptionId);
        if (sub == null) return Result.fail(Collections.singletonMap("notFound", "Subscription " + subscriptionId + " introuvable"));

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
        if (o == null) return Result.fail(Collections.singletonMap("notFound", "Order " + orderId + " introuvable"));

        Subscription sub = fetchLinkedSubscription(orderId);
        if (sub == null) return Result.fail(Collections.singletonMap("subscription", "Pas d'abonnement lié à la commande"));

        UsersSubscription us = fetchUsersSubscriptionFor(o.getUser().getId(), sub.getId());
        if (us == null) return Result.fail(Collections.singletonMap("subscription", "Aucun UsersSubscription pour cet abonnement"));

        LocalDate today = LocalDate.now();
        boolean dateOk = !today.isBefore(us.getStartDate()) && !today.isAfter(us.getEndDate());
        if (!us.isActive() || !dateOk) return Result.fail(Collections.singletonMap("subscription", "Abonnement inactif ou expiré"));
        if (us.getQuantityMax() <= 0) return Result.fail(Collections.singletonMap("subscription", "Solde insuffisant"));

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
        if (o == null) return Result.fail(Collections.singletonMap("notFound", "Order " + orderId + " introuvable"));

        o.setStatus(OrderStatus.CONFIRMED);
        em.merge(o);

        log.info("Order.confirmByOnlinePayment ok orderId=" + orderId);
        return Result.ok(o);
    }

    @Override
    public Result<Order> cancel(int orderId) {
        Order o = em.find(Order.class, orderId);
        if (o == null) return Result.fail(Collections.singletonMap("notFound", "Order " + orderId + " introuvable"));

        if (o.getStatus() == OrderStatus.CONFIRMED) {
            Subscription sub = fetchLinkedSubscription(orderId);
            if (sub != null) {
                UsersSubscription us = fetchUsersSubscriptionFor(o.getUser().getId(), sub.getId());
                if (us != null) {
                    us.setQuantityMax(us.getQuantityMax() + 1); // recrédit
                    em.merge(us);
                }
            } else {
                log.info("Order.cancel: remboursement carte à faire (orderId=" + orderId + ")");
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
        if (o == null) return Result.fail(Collections.singletonMap("notFound", "Order " + orderId + " introuvable"));
        if (o.getStatus() != OrderStatus.ONHOLD)
            return Result.fail(Collections.singletonMap("status", "La commande n'est pas ONHOLD"));

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
