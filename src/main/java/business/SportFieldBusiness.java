package business;


import Tools.Result;
import dto.Page;
import entities.SportField;
import enums.Scope;
import services.SportFieldServiceImpl;

import java.util.List;

public class SportFieldBusiness {
    private SportFieldServiceImpl sportFieldService;
    // Log4j
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SportFieldBusiness.class);

    public SportFieldBusiness(SportFieldServiceImpl sportFieldService) {
        this.sportFieldService = sportFieldService;
    }

    /**
     * Pagination ALL Sports
     * @param page
     * @param size
     * @return
     */
    public Result<Page<SportField>> getAllSportFieldPaged(int page, int size, Scope scope) {

        int pageNumber = Math.max(1, page);
        int pageSize = Math.max(1, size);
        int offset = (pageNumber - 1) * pageSize;

        Result<List<SportField>> content = scope == Scope.ALL?
                paginationGetAllSportField(offset,pageSize):
                paginationGetAllActiveSportField(offset, pageSize);
        if(!content.isSuccess()) {
            log.error(content.getErrors());
            return Result.fail(content.getErrors());
        }

        Result<Long> total = (scope == Scope.ALL)
                ? sportFieldService.countAllSportFields()
                : sportFieldService.countActiveSportField();
        if(!total.isSuccess()) {
            log.error(content.getErrors());
            return Result.fail(total.getErrors());
        }

        // Création de la page
        Page<SportField> pageObj = Page.of(content.getData(), pageNumber, pageSize, total.getData());
        log.info("Pagination réussie[allSpors] → " + pageObj);
        return Result.ok(pageObj);
    }

    /**
     * Retrieves Sport for Pagination
     * @param page
     * @param size
     * @return
     */
    private Result<List<SportField>> paginationGetAllSportField(int page, int size) {
        return sportFieldService.getAllSportFields(page, size);
    }

    /**
     * Retrieves ActiveSport for Pagination
     * @param page
     * @param size
     * @return
     */
    private Result<List<SportField>> paginationGetAllActiveSportField(int page, int size) {
        return sportFieldService.getAllActiveSportFields(page, size);
    }
}
