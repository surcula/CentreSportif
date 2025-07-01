package services;

import Tools.Result;
import dto.HallCreateForm;
import entities.Hall;
import mappers.HallMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HallServiceImpl implements interfaces.HallService {

    private final EntityManager em;
    // Log4j
    private static Logger log = Logger.getLogger(HallServiceImpl.class);

    public HallServiceImpl(EntityManager em) {

        this.em = em;
    }

    @Override
    public Result create(HallCreateForm hallCreateForm) {
        em.persist(HallMapper.fromCreateForm(hallCreateForm));
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
    public Result delete(Hall hall) {
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
    public Result<List<Hall>> getAllActiveHalls() {
        List<Hall> halls = em.createNamedQuery("getAllActiveHalls", Hall.class).getResultList();
        log.info("getAllActiveHalls : " + halls.size());
        return Result.ok(halls);
    }
}
