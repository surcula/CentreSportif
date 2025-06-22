package services;

import dto.HallCreateForm;
import entities.Hall;
import mappers.HallMapper;

import javax.persistence.EntityManager;
import java.util.List;

public class HallServiceImpl implements interfaces.HallService {

    private final EntityManager em;

    public HallServiceImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void create(HallCreateForm hallCreateForm) {
        em.persist(HallMapper.fromCreateForm(hallCreateForm));
    }

    @Override
    public void update(Hall hall) {
        em.merge(hall);
    }

    @Override
    public void delete(Hall hall) {
        em.merge(hall);
    }

    @Override
    public Hall getOneById(int id) {
        return em.find(Hall.class, id);
    }

    @Override
    public List<Hall> getAllHalls() {
        return em.createQuery("select h from Hall h", Hall.class).getResultList();
    }
}
