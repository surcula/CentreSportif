package services;

import Tools.Result;
import dto.HallCreateForm;
import entities.Hall;
import mappers.HallMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.util.List;

public class HallServiceImpl implements interfaces.HallService {

    private final EntityManager em;
    // Log4j
    private static Logger log = Logger.getLogger(EntityFinderImpl.class);

    public HallServiceImpl(EntityManager em) {

        this.em = em;
    }

    @Override
    public Result create(HallCreateForm hallCreateForm) {

        em.persist(HallMapper.fromCreateForm(hallCreateForm));
        return Result.ok();
    }

    @Override
    public Result update(Hall hall) {
        em.merge(hall);
        return Result.ok();
    }

    @Override
    public Result delete(Hall hall) {
        em.merge(hall);
        return Result.ok();
    }

    @Override
    public Result<Hall> getOneById(int id) {
        return Result.ok(em.find(Hall.class, id));
    }

    @Override
    public Result<List<Hall>> getAllActiveHalls() {
        return Result.ok(em.createNamedQuery("getAllActiveHalls", Hall.class).getResultList());
    }
}
