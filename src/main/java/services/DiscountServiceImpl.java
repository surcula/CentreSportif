package services;

import Tools.Result;
import entities.Discount;
import interfaces.DiscountService;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.*;

public class DiscountServiceImpl implements DiscountService {

    private final EntityManager em;
    private static final Logger log = Logger.getLogger(DiscountServiceImpl.class);

    public DiscountServiceImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Result<Discount> create(Discount discount) {
        em.persist(discount);
        log.info("Discount.create id=" + discount.getId());
        return Result.ok(discount);
    }

    @Override
    public Result<Discount> findOne(int id) {
        Discount d = em.find(Discount.class, id);
        if (d == null) return Result.fail(Collections.singletonMap("notFound","Discount "+id+" introuvable"));
        return Result.ok(d);
    }

    @Override
    public Result<Discount> findByName(String name) {
        List<Discount> res = em.createNamedQuery("Discount.findByName", Discount.class)
                .setParameter("n", name)
                .setMaxResults(1)
                .getResultList();
        if (res.isEmpty()) return Result.fail(Collections.singletonMap("notFound","Discount '"+name+"' introuvable"));
        return Result.ok(res.get(0));
    }

    @Override
    public Result<List<Discount>> findAllActives(Instant now) {
        List<Discount> list = em.createNamedQuery("Discount.findAllActive", Discount.class)
                .getResultList();
        log.info("Discount.findAllActives size=" + list.size());
        return Result.ok(list);
    }

    @Override
    public Result<Boolean> isApplicable(Discount discount, Instant now) {
        boolean ok = discount != null && discount.isActive();
        return Result.ok(Boolean.valueOf(ok));
    }
}
