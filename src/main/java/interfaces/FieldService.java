package interfaces;

import Tools.Result;
import Tools.Result;
import entities.Field;

import java.util.List;

public interface FieldService {
    /**
     * Create a new field
     * @param fieldCreateForm
     */
    Result create(Field fieldCreateForm) ;

    /**
     * Update an existing field
     * @param field
     */
    Result update(Field field) ;

    /**
     * soft-delete a field by setting its isActive flag to false
     * @param field
     */
    Result softDelete(Field field) ;

    /**
     * retrieves a field by its id
     * @param id
     * @return Field
     */
    Result<Field> getOneById(int id);

    /**
     * retrieves all fields
     * @return a list of all fields
     */
    Result<List<Field>> getAllFields(int page, int size) ;

    /**
     * retrieves all ACTIVE fields
     * @return a list of all ACTIVE fields
     */
    Result<List<Field>> getAllActiveFields(int page, int size) ;

    /**
     * count all fields
     * @return a long
     */
    Result<Long> countAllFields() ;

    /**
     * count all ACTIVE fields
     * @return a long
     */
    Result<Long> countAllActiveFields() ;

}
