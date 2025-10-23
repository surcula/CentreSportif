package services;

import Tools.Result;
import entities.Closure;
import interfaces.ClosureService;
import org.apache.log4j.Logger;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClosureServiceImpl implements ClosureService {

    private final EntityManager em;
    // Log4j
    private static Logger log = Logger.getLogger(EntityFinderImpl.class);

    public ClosureServiceImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Result create(Closure closureCreateForm) {
        em.persist(closureCreateForm);
        log.info("Closure created");
        return Result.ok();
    }

    @Override
    public Result update(Closure closure) {

        em.merge(closure);
        log.info("Closure updated");
        return Result.ok();
    }

    @Override
    public Result softDelete(Closure closure) {

        em.merge(closure);
        log.info("Closure : " + closure.getId() + " deleted");
        return Result.ok();
    }

    @Override
    public Result<Closure> getOneById(int id) {
        Closure closure = em.find(Closure.class, id);
        if (closure == null) {
            log.warn("Closure " + id + " not found");
            Map<String, String> errors = new HashMap<>();
            errors.put("notFound", "Aucun closure trouvé avec l’ID " + id);
            return Result.fail(errors);
        }
        log.info("Hall " + closure.getId() + " found");
        return Result.ok(closure);
    }

    @Override
    public Result<List<Closure>> getAllActiveClosures(int offset, int size) {
        List<Closure> closures = em.createNamedQuery("getAllActiveClosures", Closure.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
        log.info("getAllActiveHalls : " + closures.size());
        return Result.ok(closures);
    }

    @Override
    public Result<List<Closure>> getAllClosures(int offset, int size) {
        List<Closure> closures = em.createNamedQuery("getAllClosures", Closure.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
        log.info("getAllActiveHalls : " + closures.size());
        return Result.ok(closures);
    }

    @Override
    public Result<Long> countActiveClosures() {
        long countAllActiveClosures = em.createNamedQuery("countAllActiveClosures", Long.class)
                        .getSingleResult();
        log.info("getAllActiveClosures : " + countAllActiveClosures);
        return Result.ok(countAllActiveClosures);
    }

    @Override
    public Result<Long> countAllClosures() {
        long countAllClosures = em.createNamedQuery("countAllClosures", Long.class)
                .getSingleResult();
        log.info("getAllActiveClosures : " + countAllClosures);
        return Result.ok(countAllClosures);
    }
}
