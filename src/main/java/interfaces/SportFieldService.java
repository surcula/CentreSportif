package interfaces;

import Tools.Result;
import entities.Hall;
import entities.SportField;

import java.util.List;

public interface SportFieldService {

    /**
     * Create a new sportField
     * @param sportFieldCreateForm
     */
    Result create(SportField sportFieldCreateForm) ;

    /**
     * Update an existing sportField
     * @param sportField
     */
    Result update(SportField sportField) ;

    /**
     * soft-delete a sportField by setting its isActive flag to false
     * @param sportField
     */
    Result softDelete(SportField sportField) ;

    /**
     * retrieves a sportField by its Id
     * @param id
     * @return SportField
     */
    Result<SportField> getOneById(int id);

    /**
     * retrieves all ACTIVE SportFields
     * @return a list of all SportFields
     */
    Result<List<SportField>> getAllActiveSportFields(int offset, int size) ;

    /**
     * retrieves all SportFields
     * @return a list of all SportFields
     */
    Result<List<SportField>> getAllSportFields(int offset, int size) ;

    /**
     * count all active SportFields
     * @return number
     */
    Result<Long> countActiveSportField() ;

    /**
     * count all SportFields
     * @return number
     */
    Result<Long> countAllSportFields() ;
}
