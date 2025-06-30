package interfaces;

import Tools.Result;
import dto.HallCreateForm;
import entities.Hall;

import java.util.List;

public interface HallService {

    /**
     * Create a new hall
     * @param hallCreateForm
     */
    Result create(HallCreateForm hallCreateForm) ;

    /**
     * Update an existing hall
     * @param hall
     */
    Result update(Hall hall) ;

    /**
     * soft-delete a hall by setting its isActive flag to false
     * @param hall
     */
    Result delete(Hall hall) ;

    /**
     * retrieves a hall by its id
     * @param id
     * @return Hall
     */
    Result<Hall> getOneById(int id);

    /**
     * retrieves all halls
     * @return a list of all halls
     */
    Result<List<Hall>> getAllActiveHalls() ;
}
