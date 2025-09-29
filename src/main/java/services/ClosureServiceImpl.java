package services;

import Tools.Result;
import dto.ClosureCreateForm;
import entities.Closure;
import interfaces.ClosureService;
import mappers.ClosureMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.util.List;

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
        return Result.ok();
    }

    @Override
    public Result update(Closure closure) {

        em.merge(closure);
        return Result.ok();
    }

    @Override
    public Result softDelete(Closure closure) {

        em.merge(closure);
        return Result.ok();
    }

    @Override
    public Result<Closure> getOneById(int id) {

        return Result.ok(em.find(Closure.class, id));
    }

    @Override
    public Result<List<Closure>> getAllActiveClosures(int page, int size) {
        return null;
    }

    @Override
    public Result<List<Closure>> getAllClosures(int page, int size) {
        return null;
    }

    @Override
    public Result<Long> countActiveClosures() {
        return null;
    }

    @Override
    public Result<Long> countAllClosures() {
        return null;
    }


}
