package services;

import Tools.Result;
import entities.Field;
import interfaces.FieldService;
import org.apache.log4j.Logger;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldServiceImpl implements FieldService {
    private final EntityManager em;
    // Log4j
    private static Logger log = Logger.getLogger(FieldServiceImpl.class);

    public FieldServiceImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Result create(Field fieldCreateForm) {

        em.persist(fieldCreateForm);
        log.info("Field created");
        return Result.ok();
    }

    @Override
    public Result update(Field field) {

        em.merge(field);
        log.info("Field updated");
        return Result.ok();
    }

    @Override
    public Result softDelete(Field field) {

        em.merge(field);
        log.info("Field deleted");
        return Result.ok();
    }

    @Override
    public Result<Field> getOneById(int id) {

        Field field = em.find(Field.class, id);
        if (field != null) {
            log.info("Field found");
            return Result.ok(field);
        }else  {
            log.warn("field " + id + " not found");
            Map<String, String> errors = new HashMap<>();
            errors.put("notFound", "Aucun field trouvé avec l’ID " + id);
            return Result.fail(errors);
        }
    }

    @Override
    public Result<List<Field>> getAllFields(int offset, int size) {
        List<Field> fields = em.createNamedQuery("getAllFields", Field.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
        log.info("getAllHalls : " + fields.size());
        return Result.ok(fields);
    }

    @Override
    public Result<List<Field>> getAllActiveFields(int offset, int size) {
        List<Field> fields = em.createNamedQuery("getAllActiveFields", Field.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
        log.info("getAllActiveFields : " + fields.size());
        return Result.ok(fields);
    }

    @Override
    public Result<Long> countAllFields() {
        Long countAllFields = em.createNamedQuery("countAllFields", Long.class).getSingleResult();
        log.info("getAllFields : " + countAllFields);
        return Result.ok(countAllFields);
    }

    @Override
    public Result<Long> countAllActiveFields() {
        Long countAllFields = em.createNamedQuery("countAllActiveFields", Long.class).getSingleResult();
        log.info("countAllActiveFields : " + countAllFields);
        return Result.ok(countAllFields);
    }

}