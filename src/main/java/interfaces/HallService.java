package interfaces;

import Tools.Result;
import dto.HallCreateForm;
import dto.Page;
import entities.Hall;

import java.util.List;

public interface HallService {

    /**
     * Create a new hall
     * @param hallCreateForm
     */
    Result create(Hall hallCreateForm) ;

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
     * @return a list of all halls
     */
    Result<Long> countActiveHalls() ;

    /**
     * count all halls
     * @return a list of all halls
     */
    Result<Long> countAllHalls() ;

}
