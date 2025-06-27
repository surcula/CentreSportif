package services;

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
    public void create(ClosureCreateForm closureCreateForm) {
        em.persist(ClosureMapper.fromCreateForm(closureCreateForm));
    }

    @Override
    public void update(Closure closure) {
        em.merge(closure);
    }

    @Override
    public void delete(Closure closure) {
        em.merge(closure);
    }

    @Override
    public Closure getOneById(int id) {
        return em.find(Closure.class, id);
    }

    @Override
    public List<Closure> getAllClosures() {
        return em.createQuery("Select c from Closure c", Closure.class).getResultList();
    }
}
