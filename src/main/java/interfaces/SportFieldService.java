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
     * retrieves all ACTIVE halls
     * @return a list of all halls
     */
    Result<List<Hall>> getAllActiveHalls(int page, int size) ;

    /**
     * retrieves all halls
     * @return a list of all halls
     */
    Result<List<Hall>> getAllHalls(int page, int size) ;

    /**
     * count all active halls
     * @return number
     */
    Result<Long> countActiveHalls() ;

    /**
     * count all halls
     * @return number
     */
    Result<Long> countAllHalls() ;
}
