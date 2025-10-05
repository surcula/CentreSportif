package interfaces;

import Tools.Result;
import entities.Sport;

import java.util.List;

public interface SportService {

    /**
     * Create a new sport
     * @param sportCreateForm
     */
    Result create(Sport sportCreateForm) ;

    /**
     * Update an existing sport
     * @param sport
     */
    Result update(Sport sport) ;

    /**
     * soft-delete a sport by setting its isActive flag to false
     * @param sport
     */
    Result softDelete(Sport sport) ;

    /**
     * retrieve a sport by its Id
     * @param id
     * @return Sport
     */
    Result<Sport> getOneById(int id);

    /**
     * retrieves all ACTIVE Sports
     * @return a list of all Sports
     */
    Result<List<Sport>> getAllActiveSports(int page, int size) ;

    /**
     * retrieves all Sports
     * @return a list of all Sports
     */
    Result<List<Sport>> getAllSports(int page, int size) ;

    /**
     * count all active Sports
     * @return number
     */
    Result<Long> countActiveSports() ;

    /**
     * count all Sports
     * @return number
     */
    Result<Long> countAllSports() ;
}
