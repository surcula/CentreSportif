package services;

import Tools.Result;
import entities.SportField;
import interfaces.SportFieldService;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.util.List;

public class SportFieldServiceImpl implements SportFieldService {
    private final EntityManager em;
    // Log4j
    private static Logger log = Logger.getLogger(SportFieldServiceImpl.class);

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
    public Result<List<SportField>> getAllActiveSportFields(int offset, int size) {
        List<SportField> sportFields = em.createNamedQuery("getAllActiveSportFields", SportField.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
        log.info("getAllActiveHalls : " + sportFields.size());
        return Result.ok(sportFields);
    }

    @Override
    public Result<List<SportField>> getAllSportFields(int offset, int size) {
        List<SportField> sportFields = em.createNamedQuery("getAllSportFields", SportField.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
        log.info("getAllActiveHalls : " + sportFields.size());
        return Result.ok(sportFields);
    }

    @Override
    public Result<Long> countActiveSportField() {
        Long countSportFields = em.createNamedQuery("countAllActiveSportFields", Long.class)
                        .getSingleResult();
        log.info("getAllActiveHalls : " + countSportFields);
        return Result.ok(countSportFields);
    }

    @Override
    public Result<Long> countAllSportFields() {
        Long countSportFields = em.createNamedQuery("countAllSportFields", Long.class)
                .getSingleResult();
        log.info("getAllHalls : " + countSportFields);
        return Result.ok(countSportFields);
    }


}
