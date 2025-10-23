package interfaces;

import Tools.Result;
import entities.Closure;

import java.util.List;

public interface ClosureService {
    /**
     * Create a new closure
     * @param closureCreateForm
     */
    Result create(Closure closureCreateForm) ;

    /**
     * Update an existing closure
     * @param closure
     */
    Result update(Closure  closure) ;

    /**
     * soft-delete a closure by setting its isActive flag to false
     * @param closure
     */
    Result softDelete(Closure closure) ;

    /**
     * retrieves a closure by its Id
     * @param id
     * @return Closure
     */
    Result<Closure> getOneById(int id);

    /**
     * retrieves all ACTIVE closure
     * @return a list of all closures
     */
    Result<List<Closure>> getAllActiveClosures(int offset, int size) ;

    /**
     * retrieves all closures
     * @return a list of all closures
     */
    Result<List<Closure>> getAllClosures(int offset, int size) ;

    /**
     * count all active halls
     * @return number
     */
    Result<Long> countActiveClosures() ;

    /**
     * count all closures
     * @return number
     */
    Result<Long> countAllClosures() ;
}
