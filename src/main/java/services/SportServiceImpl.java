package services;

import dto.SportCreateForm;
import entities.Sport;
import interfaces.SportService;
import mappers.SportMapper;

import javax.persistence.EntityManager;
import java.util.List;

public class SportServiceImpl implements SportService {
    private final EntityManager em;

    public SportServiceImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void create(SportCreateForm sportCreateForm) {
        em.persist(SportMapper.fromCreateForm(sportCreateForm));
    }

    @Override
    public void update(Sport sport) {
        em.merge(sport);
    }

    @Override
    public void delete(Sport sport) {
        em.merge(sport);
    }

    @Override
    public Sport getOneById(int id) {
        return em.find(Sport.class, id);
    }

    @Override
    public List<Sport> getAllSports() {
        return em.createQuery("Select s from Sport s", Sport.class).getResultList();
    }
}
