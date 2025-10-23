package services;

import Tools.Result;
import entities.Hall;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HallServiceImpl implements interfaces.HallService {

    private final EntityManager em;
    // Log4j
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HallServiceImpl.class);


    public HallServiceImpl(EntityManager em) {

        this.em = em;
    }

    @Override
    public Result create(Hall hallCreateForm) {
        em.persist(hallCreateForm);
        log.info("Hall created");
        return Result.ok();
    }

    @Override
    public Result update(Hall hall) {
        em.merge(hall);
        log.info("Hall " + hall.getId() + " updated");
        return Result.ok();
    }

    @Override
    public Result softDelete(Hall hall) {
        em.merge(hall);
        log.info("Hall " + hall.getId() + " is not active");
        return Result.ok();
    }

    @Override
    public Result<Hall> getOneById(int id) {
        /**
         * Il faut vériffier si l'objet existe no?-> erreur
         */
        Hall hall = em.find(Hall.class, id);
        if (hall == null) {
            log.warn("Hall " + id + " not found");
            Map<String, String> errors = new HashMap<>();
            errors.put("notFound", "Aucun hall trouvé avec l’ID " + id);
            return Result.fail(errors);
        }
        log.info("Hall " + hall.getId() + " found");
        return Result.ok(hall);
    }

    @Override
    public Result<List<Hall>> getAllActiveHalls(int offset, int size) {
        offset = Math.max(offset, 0);
        if(size <= 0){
            List<Hall> halls = em.createNamedQuery("getAllActiveHalls", Hall.class)
                    .getResultList();
            log.info("getAllActiveHalls : " + halls.size());
            return Result.ok(halls);
        }
        List<Hall> halls = em.createNamedQuery("getAllActiveHalls", Hall.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
        log.info("getAllActiveHalls : " + halls.size());
        return Result.ok(halls);
    }

    @Override
    public Result<List<Hall>> getAllHalls(int offset, int size) {
        offset = Math.max(offset, 0);
        if(size <= 0){
            List<Hall> halls = em.createNamedQuery("getAllHalls", Hall.class)
                    .getResultList();
            log.info("getAllHalls : " + halls.size());
            return Result.ok(halls);
        }
        List<Hall> halls = em.createNamedQuery("getAllHalls", Hall.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
        log.info("getAllHalls : " + halls.size());
        return Result.ok(halls);
    }

    @Override
    public Result<Long> countActiveHalls() {
        Long countAllHalls = em.createNamedQuery("countAllActiveHalls", Long.class).getSingleResult();
        log.info("getAllHalls : " + countAllHalls);
        return Result.ok(countAllHalls);
    }

    @Override
    public Result<Long> countAllHalls() {
        Long countAllHalls = em.createNamedQuery("countAllHalls", Long.class).getSingleResult();
        log.info("getAllHalls : " + countAllHalls);
        return Result.ok(countAllHalls);
    }


}
