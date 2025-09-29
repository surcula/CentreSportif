package services;

import Tools.Result;
import entities.Hall;
import entities.SportField;
import interfaces.SportFieldService;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.util.List;

public class SportFieldServiceImpl implements SportFieldService {
    private final EntityManager em;
    // Log4j
    private static Logger log = Logger.getLogger(EntityFinderImpl.class);

    public SportFieldServiceImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Result create(SportField sportFieldCreateForm) {
        em.persist(sportFieldCreateForm);
        return Result.ok();
    }
    @Override
    public Result update(SportField sportField) {
        em.merge(sportField);
        return Result.ok();
    }

    @Override
    public Result softDelete(SportField sportField) {
        em.merge(sportField);
        return Result.ok();
    }

    @Override
    public Result<SportField> getOneById(int id) {

        return Result.ok(em.find(SportField.class, id));
    }

    @Override
    public Result<List<Hall>> getAllActiveHalls(int page, int size) {
        return null;
    }

    @Override
    public Result<List<Hall>> getAllHalls(int page, int size) {
        return null;
    }

    @Override
    public Result<Long> countActiveHalls() {
        return null;
    }

    @Override
    public Result<Long> countAllHalls() {
        return null;
    }


}
