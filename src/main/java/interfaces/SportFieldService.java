package interfaces;

import dto.SportFieldCreateForm;
import entities.SportField;

import java.util.List;

public interface SportFieldService {

    /**
     * Create a new sportField
     * @param sportFieldCreateForm
     */
    void create(SportFieldCreateForm sportFieldCreateForm) ;

    /**
     * Update an existing sportField
     * @param sportField
     */
    void update(SportField sportField) ;

    /**
     * soft-delete a sportField by setting its isActive flag to false
     * @param sportField
     */
    void delete(SportField sportField) ;

    /**
     * retrieves a sportField by its Id
     * @param id
     * @return SportField
     */
    SportField getOneById(int id);

    /**
     * retrieves all SportFields
     * @return a list of all SportFields
     */
    List<SportField> getAllSportFields() ;
}
