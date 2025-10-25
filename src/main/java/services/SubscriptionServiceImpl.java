package services;

import Tools.Result;
import entities.UsersSubscription;
import interfaces.SubscriptionService;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.*;

public class SubscriptionServiceImpl implements SubscriptionService {

    private final EntityManager em;
    private static final Logger log = Logger.getLogger(SubscriptionServiceImpl.class);

    public SubscriptionServiceImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Result<UsersSubscription> assignToUser(int subscriptionId, int userId, LocalDate start, LocalDate end, int quantity) {
        // Implémentation back-office plus tard (création UsersSubscription)
        return Result.fail(Collections.singletonMap("todo","assignToUser non implémenté"));
    }

    @Override
    public Result<UsersSubscription> findUsersSubscription(int id) {
        UsersSubscription us = em.find(UsersSubscription.class, id);
        if (us == null) return Result.fail(Collections.singletonMap("notFound","UsersSubscription "+id+" introuvable"));
        return Result.ok(us);
    }

    @Override
    public Result<List<UsersSubscription>> findByUser(int userId) {
        List<UsersSubscription> list = em.createNamedQuery("UsersSubscription.byUser", UsersSubscription.class)
                .setParameter("uid", userId)
                .getResultList();
        log.info("UsersSubscription.findByUser size=" + list.size() + " userId=" + userId);
        return Result.ok(list);
    }

    @Override
    public Result<Boolean> isValid(UsersSubscription us, LocalDate now) {
        boolean ok = us != null && us.isActive()
                && !now.isBefore(us.getStartDate())
                && !now.isAfter(us.getEndDate())
                && us.getQuantityMax() > 0;
        return Result.ok(Boolean.valueOf(ok));
    }

    @Override
    public Result<Boolean> debit(UsersSubscription us, int qty) {
        if (us == null) return Result.fail(Collections.singletonMap("subscription","UsersSubscription null"));
        UsersSubscription managed = em.find(UsersSubscription.class, us.getId());
        if (managed.getQuantityMax() < qty) return Result.ok(Boolean.FALSE);
        managed.setQuantityMax(managed.getQuantityMax() - qty);
        em.merge(managed);
        log.info("UsersSubscription.debit -"+qty+" id="+managed.getId()+" newQty="+managed.getQuantityMax());
        return Result.ok(Boolean.TRUE);
    }

    @Override
    public Result<Boolean> credit(UsersSubscription us, int qty) {
        if (us == null) return Result.fail(Collections.singletonMap("subscription","UsersSubscription null"));
        UsersSubscription managed = em.find(UsersSubscription.class, us.getId());
        managed.setQuantityMax(managed.getQuantityMax() + qty);
        em.merge(managed);
        log.info("UsersSubscription.credit +"+qty+" id="+managed.getId()+" newQty="+managed.getQuantityMax());
        return Result.ok(Boolean.TRUE);
    }
}
