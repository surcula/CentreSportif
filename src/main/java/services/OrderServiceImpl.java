package services;

import Tools.Result;
import business.OrderBusiness;
import entities.Discount;
import entities.Order;
import entities.OrdersDiscount;
import entities.OrdersSubscription;
import entities.Subscription;
import entities.User;
import entities.UsersSubscription;
import enums.OrderStatus;
import interfaces.OrderService;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Commandes
 * - CRUD + listing
 * - Devis sport
 * - Liaisons Remises / Abonnements
 * - Paiement via abonnement / en ligne
 * - Annulation / suppression
 */
public class OrderServiceImpl implements OrderService {

    private final EntityManager em;
    private static final Logger log = Logger.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(EntityManager em) {
        this.em = em;
    }

    /* =========================
     *          CRUD
     * ========================= */

    @Override
    public Result<Order> create(Order order) {
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
        List<Order> list = em.createNamedQuery("Order.findAll", Order.class)
                .getResultList();
        log.info("Order.findAll size=" + list.size());
        return Result.ok(list);
    }

    @Override
    public Result<List<Order>> getAllOrders(int offset, int limit) {
        List<Order> list = em.createNamedQuery("Order.findAll", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        log.info("Order.getAllOrders size=" + list.size() + " offset=" + offset + " limit=" + limit);
        return Result.ok(list);
    }

    @Override
    public Result<Long> countAllOrders() {
        Long total = em.createNamedQuery("Order.countAll", Long.class).getSingleResult();
        log.info("Order.countAllOrders = " + total);
        return Result.ok(total);
    }

    @Override
    public Result<List<Order>> findByUser(int userId, int offset, int limit) {
        List<Order> list = em.createNamedQuery("Order.findByUser", Order.class)
                .setParameter("uid", userId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        log.info("Order.findByUser size=" + list.size() + " userId=" + userId + " offset=" + offset + " limit=" + limit);
        return Result.ok(list);
    }

    @Override
    public Result<Long> countByUser(int userId) {
        Long c = em.createNamedQuery("Order.countByUser", Long.class)
                .setParameter("uid", userId)
                .getSingleResult();
        log.info("Order.countByUser = " + c + " userId=" + userId);
        return Result.ok(c);
    }

    /* =========================
     *         DEVIS SPORT
     * ========================= */

    /**
     * Devis: prix = (PU du sport à la date 'forDate') * quantity.
     * Si la table 'historicals_sports_prices' est absente/incompatible,
     * on ignore l'historique et on garde le prix courant du sport.
     */
    @Override
    public Result<Map<String, Double>> quoteFromSport(int sportId, int quantity, LocalDate forDate) {
        try {
            if (quantity <= 0) {
                return Result.fail(Collections.singletonMap("quantity", "quantité invalide"));
            }
            LocalDate when = (forDate != null) ? forDate : LocalDate.now();

            // 1) prix unitaire courant depuis la table sports (NamedQuery)
            Double unit = em.createNamedQuery("Sport.getUnitPriceById", Double.class)
                    .setParameter("sid", sportId)
                    .getSingleResult();

            // 2) tentative d'historique (optionnelle). En cas d'erreur => on garde le PU courant.
            try {
                Object r = em.createNativeQuery(
                                "SELECT h.price " +
                                        "FROM historicals_sports_prices h " +
                                        "WHERE h.sport_id = ? AND h.start_date <= ? " +
                                        "ORDER BY h.start_date DESC LIMIT 1")
                        .setParameter(1, sportId)
                        .setParameter(2, java.sql.Date.valueOf(when))
                        .getSingleResult();

                if (r != null) unit = ((Number) r).doubleValue();
            } catch (Exception ignore) {
                // table absente / schéma différent : on n'écrase pas 'unit'
            }

            double total = unit * quantity;
            Map<String, Double> out = new HashMap<>();
            out.put("unit", unit);
            out.put("total", total);
            return Result.ok(out);

        } catch (Exception e) {
            log.error("quoteFromSport failed", e);
            return Result.fail(Collections.singletonMap("error", "Erreur devis: " + e.getMessage()));
        }
    }

    /* =========================
     *   BUSINESS: COMMANDES
     * ========================= */

    /**
     * Crée une commande ONHOLD depuis un UsersSubscription et l’attache via OrdersSubscription.
     * Affiche ensuite l’order dans le form (OrderServlet fait le forward).
     */
    @Override
    public Result<Order> createDraftFromSubscription(int usersSubscriptionId) {
        Map<String,String> errors = new HashMap<>();
        try {
            if (usersSubscriptionId <= 0) {
                errors.put("subscription", "identifiant abonnement invalide");
                return Result.fail(errors);
            }

            // Charger UsersSubscription + user + subscription (NamedQuery défini dans UsersSubscription)
            UsersSubscription us;
            try {
                us = em.createNamedQuery("UsersSubscription.findByIdWithUserAndSubscription", UsersSubscription.class)
                        .setParameter("id", usersSubscriptionId)
                        .getSingleResult();
            } catch (NoResultException nre) {
                errors.put("subscription", "abonnement introuvable");
                return Result.fail(errors);
            }

            // Validations basiques
            LocalDate today = LocalDate.now();
            if (!us.isActive()) errors.put("subscription", "abonnement inactif");
            if (us.getStartDate()!=null && today.isBefore(us.getStartDate()))
                errors.put("subscription", "abonnement pas encore commencé");
            if (us.getEndDate()!=null && today.isAfter(us.getEndDate()))
                errors.put("subscription", "abonnement expiré");
            if (!errors.isEmpty()) return Result.fail(errors);

            // Créer la commande ONHOLD
            Order o = new Order();
            o.setStatus(OrderStatus.ONHOLD);
            // ➜ adapte le type si tu as LocalDateTime dans l’entité
            o.setDateOrder(java.time.Instant.now());
            o.setUser(us.getUser());
            o.setTotalPrice(0.0);
            em.persist(o);

            // Lier via la table pivot OrdersSubscription
            OrdersSubscription link = new OrdersSubscription();
            link.setOrder(o);
            link.setSubscription(us.getSubscription());
            // Si ton entité pivot possède aussi un champ UsersSubscription, set-le ici :
            // link.setUsersSubscription(us);
            link.setActive(true);
            link.setCreatedAt(Instant.now());
            link.setUpdatedAt(Instant.now());
            em.persist(link);

            em.flush();
            log.info("Order.createDraftFromSubscription ok orderId=" + o.getId() + " usId=" + usersSubscriptionId);
            return Result.ok(o);

        } catch (Exception e) {
            errors.put("error", e.getMessage());
            return Result.fail(errors);
        }
    }

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

        // 👉 NamedQuery au lieu de SELECT direct
        List<OrdersDiscount> ods = em.createNamedQuery("OrdersDiscount.findActiveByOrder", OrdersDiscount.class)
                .setParameter("oid", o.getId())
                .getResultList();

        Result<Double> total = OrderBusiness.computeDiscountedTotal(o.getTotalPrice(), ods);
        if (!total.isSuccess()) return Result.fail(total.getErrors());
        o.setTotalPrice(total.getData());
        em.merge(o);

        log.info("Order.applyDiscount ok orderId=" + orderId + " discountId=" + discountId);
        return Result.ok(o);
    }

    // === Remises automatiques (club + multi-abonnements) ===
    @Override
    public Result<Order> applyAutoDiscounts(Integer orderId, boolean clubEligible) {
        try {
            Order order = em.find(Order.class, orderId);
            if (order == null) {
                return Result.fail(Collections.singletonMap("notFound", "Commande introuvable ou inactive."));
            }

            // Récup des remises par NOM (doivent exister et être actives)
            Integer clubId  = findDiscountIdByName("Club -10%");
            Integer multiId = findDiscountIdByName("Multi-Abonnements -5%");

            // 1) Remise club -10% si éligible
            if (clubEligible && clubId != null) {
                Result<Order> r1 = this.applyDiscount(orderId, clubId);
                if (!r1.isSuccess()) return r1;
                order = r1.getData();
            }

            // 2) -5% si au moins 2 abonnements liés à la commande (NamedQuery)
            Long subsCount = em.createNamedQuery("OrdersSubscription.countActiveByOrder", Long.class)
                    .setParameter("o", order)
                    .getSingleResult();

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

    // Helper interne: trouve l'id d'une remise active par son nom (NamedQuery)
    private Integer findDiscountIdByName(String name) {
        try {
            return em.createNamedQuery("Discount.findActiveIdByName", Integer.class)
                    .setParameter("n", name)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * (Optionnel) Lier un abonnement “catalogue” à une commande existante.
     * NB: pour le parcours “depuis UsersSubscription”, préfère createDraftFromSubscription(...).
     */
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

        // Débit d'une séance
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
                // TODO: remboursement PSP si paiement CB réel
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

        // Purges
        em.createQuery("DELETE FROM OrdersDiscount od WHERE od.order.id = :oid")
                .setParameter("oid", orderId).executeUpdate();
        em.createQuery("DELETE FROM OrdersSubscription os WHERE os.order.id = :oid")
                .setParameter("oid", orderId).executeUpdate();

        em.remove(o);
        log.info("Order.cancelUnpaid ok orderId=" + orderId);
        return Result.ok(o);
    }

    /* =========================
     *         HELPERS
     * ========================= */

    private Subscription fetchLinkedSubscription(int orderId) {
        try {
            return em.createNamedQuery("OrdersSubscription.findActiveSubscriptionByOrder", Subscription.class)
                    .setParameter("oid", orderId)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) { return null; }
    }

    private UsersSubscription fetchUsersSubscriptionFor(int userId, int subscriptionId) {
        try {
            return em.createNamedQuery("UsersSubscription.findActiveByUserAndSubscription", UsersSubscription.class)
                    .setParameter("uid", userId)
                    .setParameter("sid", subscriptionId)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) { return null; }
    }
}
