package interfaces;

import dto.HallCreateForm;
import entities.Hall;

import java.util.List;

public interface HallService {

    /**
     * Create a new hall
     * @param hallCreateForm
     */
    void create(HallCreateForm hallCreateForm) ;

    /**
     * Update an existing hall
     * @param hall
     */
    void update(Hall hall) ;

    /**
     * soft-delete a hall by setting its isActive flag to false
     * @param hall
     */
    void delete(Hall hall) ;

    /**
     * retrieves a hall by its id
     * @param id
     * @return Hall
     */
    Hall getOneById(int id);

    /**
     * retrieves all halls
     * @return a list of all halls
     */
    List<Hall> getAllHalls() ;
}
