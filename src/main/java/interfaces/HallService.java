package interfaces;

import Tools.Result;
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
    Result softDelete(Hall hall) ;

    /**
     * retrieves a hall by its id
     * @param id
     * @return Hall
     */
    Result<Hall> getOneById(int id);

    /**
     * Retrieves all active Halls with optional pagination.
     *
     * @param offset page number starting at 1 (values <= 0 are treated as 1)
     * @param size number of elements per page.
     *             If size smaller or equal to 0, all active halls are returned without pagination.
     * @return Result containing a list of active Halls (never null).
     */
    Result<List<Hall>> getAllActiveHalls(int offset, int size) ;

    /**
     * Retrieves all Halls with optional pagination.
     *
     * @param offset page number starting at 1 (values smaller or equal to 0 are treated as 1)
     * @param size number of elements per page.
     *             If size smaller or equal to 0, all active halls are returned without pagination.
     * @return Result containing a list of Halls (never null).
     */
    Result<List<Hall>> getAllHalls(int offset, int size) ;

    /**
     * count all active halls
     * @return a number
     */
    Result<Long> countActiveHalls() ;

    /**
     * count all halls
     * @return a number
     */
    Result<Long> countAllHalls() ;

}
