package services;

import dto.FieldCreateForm;
import entities.Field;
import interfaces.FieldService;
import mappers.FieldMapper;

import javax.persistence.EntityManager;
import java.util.List;

public class FieldServiceImpl implements FieldService {
    private final EntityManager em;

    public FieldServiceImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void create(FieldCreateForm fieldCreateForm) {
        em.persist(FieldMapper.fromCreateForm(fieldCreateForm));
    }

    @Override
    public void update(Field field) {
        em.merge(field);
    }

    @Override
    public void delete(Field field) {
        em.merge(field);
    }

    @Override
    public Field getOneById(int id) {
        return em.find(Field.class, id);
    }

    @Override
    public List<Field> getAllFields() {

        return em.createQuery("select f from Field f", Field.class).getResultList();
    }
}
