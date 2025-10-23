package services;

import Tools.Result;
import entities.Sport;
import interfaces.SportService;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.util.List;

public class SportServiceImpl implements SportService {
    private final EntityManager em;
    // Log4j
    private static Logger log = Logger.getLogger(SportServiceImpl.class);

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
    public Result<List<Sport>> getAllActiveSports(int offset, int size) {

        List<Sport> sports = em.createNamedQuery("getAllActiveSports", Sport.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
        log.info("getAllActiveHalls : " + sports.size());
        return Result.ok(sports);
    }

    @Override
    public Result<List<Sport>> getAllSports(int offset, int size) {
        List<Sport> sports = em.createNamedQuery("getAllSports", Sport.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
        log.info("getAllActiveHalls : " + sports.size());
        return Result.ok(sports);
    }

    @Override
    public Result<Long> countActiveSports() {
        Long countAllActiveSports = em.createNamedQuery("countAllActiveSports", Long.class)
                .getSingleResult();
        log.info("getAllActiveHalls : " + countAllActiveSports);
        return Result.ok(countAllActiveSports);
    }

    @Override
    public Result<Long> countAllSports() {

        Long countAllSports = em.createNamedQuery("countAllSports", Long.class)
                .getSingleResult();
        log.info("getAllActiveHalls : " + countAllSports);
        return Result.ok(countAllSports);
    }


}
