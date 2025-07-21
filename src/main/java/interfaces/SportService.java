package interfaces;

import dto.SportCreateForm;
import entities.Sport;

import java.util.List;

public interface SportService {

    /**
     * Create a new sport
     * @param sportCreateForm
     */
    void create(SportCreateForm sportCreateForm) ;

    /**
     * Update an existing sport
     * @param sport
     */
    void update(Sport sport) ;

    /**
     * soft-delete a sport by setting its isActive flag to false
     * @param sport
     */
    void delete(Sport sport) ;

    /**
     * retrieve a sport by its Id
     * @param id
     * @return Sport
     */
    Sport getOneById(int id);

    /**
     * retrieves all sports
     * @return a list of all sports
     */
    List<Sport> getAllSports() ;
}
