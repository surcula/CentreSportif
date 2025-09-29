package services;

import Tools.Result;
import dto.SportCreateForm;
import entities.Sport;
import interfaces.SportService;
import mappers.SportMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.util.List;

public class SportServiceImpl implements SportService {
    private final EntityManager em;
    // Log4j
    private static Logger log = Logger.getLogger(EntityFinderImpl.class);

    public SportServiceImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Result create(Sport sportCreateForm) {

        em.persist(sportCreateForm);
        return Result.ok();
    }

    @Override
    public Result update(Sport sport) {

        em.merge(sport);
        return Result.ok();
    }

    @Override
    public Result softDelete(Sport sport) {

        em.merge(sport);
        return Result.ok();
    }

    @Override
    public Result<Sport> getOneById(int id) {

        return Result.ok(em.find(Sport.class, id));
    }

    @Override
    public Result<List<Sport>> getAllActiveSports(int page, int size) {
        return null;
    }

    @Override
    public Result<List<Sport>> getAllSports(int page, int size) {
        return null;
    }

    @Override
    public Result<Long> countActiveSports() {
        return null;
    }

    @Override
    public Result<Long> countAllSports() {
        return null;
    }


}
