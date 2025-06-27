package services;

import dto.SportFieldCreateForm;
import entities.SportField;
import interfaces.SportFieldService;
import mappers.SportFieldMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.util.List;

public class SportFieldServiceImpl implements SportFieldService {
    private final EntityManager em;
    // Log4j
    private static Logger log = Logger.getLogger(EntityFinderImpl.class);

    public SportFieldServiceImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void create(SportFieldCreateForm sportFieldCreateForm) {
        em.persist(SportFieldMapper.fromCreateForm(sportFieldCreateForm));
    }

    @Override
    public void update(SportField sportField) {
        em.merge(sportField);

    }

    @Override
    public void delete(SportField sportField) {
        em.merge(sportField);
    }

    @Override
    public SportField getOneById(int id) {
        return em.find(SportField.class, id);
    }

    @Override
    public List<SportField> getAllSportFields() {
        return em.createQuery("Select s from SportField s", SportField.class).getResultList();
    }
}
