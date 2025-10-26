package services;

import Tools.Result;
import entities.Subscription;
import entities.User;
import entities.UsersSubscription;
import interfaces.SubscriptionService;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class SubscriptionServiceImpl implements SubscriptionService {

    private final EntityManager em;
    private static final Logger log = Logger.getLogger(SubscriptionServiceImpl.class);

    public SubscriptionServiceImpl(EntityManager em) {
        this.em = em;
    }

    /* ========= CREATE ========= */

    @Override
    public Result<UsersSubscription> assignToUser(int subscriptionId, int userId,
                                                  LocalDate start, LocalDate end, int quantity) {
        try {
            // Si l'id N'EST PAS auto-généré sur l'entité, on doit le setter :
            Integer nextId = getNextUsersSubscriptionId();

            UsersSubscription us = new UsersSubscription();
            us.setId(nextId);
            // getReference = évite un SELECT full (ok si FK existe)
            us.setSubscription(em.getReference(Subscription.class, subscriptionId));
            us.setUser(em.getReference(User.class, userId));
            us.setStartDate(start);
            us.setEndDate(end);
            us.setQuantityMax(quantity);
            us.setActive(true);

            em.persist(us);
            log.info("UsersSubscription créé: id=" + us.getId()
                    + " subId=" + subscriptionId + " userId=" + userId);
            return Result.ok(us);
        } catch (Exception e) {
            log.error("assignToUser error", e);
            return Result.fail(Collections.singletonMap("assign", e.getMessage()));
        }
    }

    /** Calcule le prochain id pour UsersSubscription (si pas @GeneratedValue). */
    private Integer getNextUsersSubscriptionId() {
        try {
            Integer max = em.createNamedQuery("UsersSubscription.maxId", Integer.class)
                    .getSingleResult();
            return max + 1;
        } catch (Exception e) {
            log.error("getNextUsersSubscriptionId error", e);
            // fallback : id pseudo-unique
            return (int) (System.currentTimeMillis() & 0x7FFFFFFF);
        }
    }

    /* ========= READ ========= */

    @Override
    public Result<UsersSubscription> findUsersSubscription(int id) {
        UsersSubscription us = em.find(UsersSubscription.class, id);
        if (us == null) return Result.fail(Collections.singletonMap("notFound", "UsersSubscription " + id + " introuvable"));
        return Result.ok(us);
    }

    @Override
    public Result<List<UsersSubscription>> findByUser(int userId) {
        List<UsersSubscription> list = em.createNamedQuery("UsersSubscription.byUser", UsersSubscription.class)
                .setParameter("uid", userId)
                .getResultList();

        log.info("findByUser size=" + list.size() + " userId=" + userId);
        return Result.ok(list);
    }

    /* ========= UPDATE (pour POST action=update) ========= */

    @Override
    public Result<UsersSubscription> updateUsersSubscription(int id, LocalDate start, LocalDate end, Integer quantity, boolean active) {
        try {
            UsersSubscription us = em.find(UsersSubscription.class, id);
            if (us == null)
                return Result.fail(Collections.singletonMap("notFound", "UsersSubscription " + id + " introuvable"));

            if (start != null) us.setStartDate(start);
            if (end != null)   us.setEndDate(end);
            if (quantity != null && quantity.intValue() >= 0) us.setQuantityMax(quantity.intValue());
            us.setActive(active);

            em.merge(us);
            log.info("UsersSubscription update id=" + id);
            return Result.ok(us);
        } catch (Exception e) {
            log.error("updateUsersSubscription error", e);
            return Result.fail(Collections.singletonMap("update", e.getMessage()));
        }
    }

    /* ========= RÈGLES D’USAGE ========= */

    @Override
    public Result<Boolean> isValid(UsersSubscription us, LocalDate now) {
        boolean ok = us != null
                && us.isActive()
                && (now.isEqual(us.getStartDate()) || now.isAfter(us.getStartDate()))
                && (now.isEqual(us.getEndDate())   || now.isBefore(us.getEndDate()))
                && us.getQuantityMax() > 0;

        return Result.ok(Boolean.valueOf(ok));
    }

    @Override
    public Result<Boolean> debit(UsersSubscription us, int qty) {
        if (us == null) return Result.fail(Collections.singletonMap("subscription","UsersSubscription null"));
        if (qty <= 0)    return Result.ok(Boolean.FALSE);

        UsersSubscription managed = em.find(UsersSubscription.class, us.getId());
        if (managed == null) return Result.fail(Collections.singletonMap("subscription","UsersSubscription introuvable"));

        if (managed.getQuantityMax() < qty) {
            log.info("debit refusé (insuffisant) id=" + managed.getId());
            return Result.ok(Boolean.FALSE);
        }
        managed.setQuantityMax(managed.getQuantityMax() - qty);
        em.merge(managed);
        log.info("debit -" + qty + " id=" + managed.getId() + " newQty=" + managed.getQuantityMax());
        return Result.ok(Boolean.TRUE);
    }

    @Override
    public Result<Boolean> credit(UsersSubscription us, int qty) {
        if (us == null) return Result.fail(Collections.singletonMap("subscription","UsersSubscription null"));
        if (qty <= 0)    return Result.ok(Boolean.FALSE);

        UsersSubscription managed = em.find(UsersSubscription.class, us.getId());
        if (managed == null) return Result.fail(Collections.singletonMap("subscription","UsersSubscription introuvable"));

        managed.setQuantityMax(managed.getQuantityMax() + qty);
        em.merge(managed);
        log.info("credit +" + qty + " id=" + managed.getId() + " newQty=" + managed.getQuantityMax());
        return Result.ok(Boolean.TRUE);
    }

    /* ========= UTIL ========= */

    /** Retourne l’US actif du user pour un subscriptionId donné, sinon null. */
    public UsersSubscription findActiveForUserAndSub(int userId, int subscriptionId, LocalDate now) {
        try {
            return em.createNamedQuery("UsersSubscription.findActiveByUserAndSubscriptionAtDate", UsersSubscription.class)
                    .setParameter("uid", userId)
                    .setParameter("sid", subscriptionId)
                    .setParameter("today", now)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
