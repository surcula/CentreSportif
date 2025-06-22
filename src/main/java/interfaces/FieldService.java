package interfaces;

import dto.FieldCreateForm;
import entities.Field;

import java.util.List;

public interface FieldService {
    /**
     * Create a new field
     * @param fieldCreateForm
     */
    void create(FieldCreateForm fieldCreateForm) ;

    /**
     * Update an existing field
     * @param field
     */
    void update(Field field) ;

    /**
     * soft-delete a field by setting its isActive flag to false
     * @param field
     */
    void delete(Field field) ;

    /**
     * retrieves a field by its id
     * @param id
     * @return Field
     */
    Field getOneById(int id);

    /**
     * retrieves all fields
     * @return a list of all fields
     */
    List<Field> getAllFields() ;
}
