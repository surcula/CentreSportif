package services;

import Tools.Result;
import dto.FieldCreateForm;
import entities.Field;
import interfaces.FieldService;
import mappers.FieldMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.util.List;

public class FieldServiceImpl implements FieldService {
    private final EntityManager em;
    // Log4j
    private static Logger log = Logger.getLogger(EntityFinderImpl.class);

    public FieldServiceImpl(EntityManager em) {

        this.em = em;

    }


    @Override
    public Result create(Field fieldCreateForm) {
        em.persist(fieldCreateForm);
        return Result.ok();
    }

    @Override
    public Result update(Field field) {

        em.merge(field);
        return Result.ok();
    }

    @Override
    public Result softDelete(Field field) {

        em.merge(field);
        return Result.ok();
    }

    @Override
    public Result<Field> getOneById(int id) {

        return Result.ok(em.find(Field.class, id));
    }

    @Override
    public Result<List<Field>> getAllFields(int page, int size) {
        return null;
    }

    @Override
    public Result<List<Field>> getAllActiveFields(int page, int size) {
        return null;
    }

    @Override
    public Result<Long> countAllFields() {
        return null;
    }

    @Override
    public Result<Long> countAllActiveFields() {
        return null;
    }


}
