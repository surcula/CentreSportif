package business;

import Tools.Result;
import dto.Page;
import entities.Hall;
import enums.Scope;
import services.HallServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HallBusiness {

    public HallBusiness(HallServiceImpl hallService) {
        this.hallService = hallService;
    }

    private HallServiceImpl hallService;
    // Log4j
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HallBusiness.class);

    /**
     * validate data submitted by CreateForm
     *
     * @param strHallName
     * @param strWidth
     * @param strLength
     * @param strHeight
     * @param strActive
     * @return
     */
    public static Result<Hall> initCreateForm(String strHallName, String strWidth, String strLength, String strHeight, String strActive) {
        Map<String, String> errors = new HashMap<>();

        String hallName = ValidateForm.stringIsEmpty(
                strHallName,
                "errorHallName",
                "Hall name",
                errors
        );

        Double height = ValidateForm.parseDouble(
                strHeight,
                "errorHeight",
                "Height",
                errors
        );

        Double length = ValidateForm.parseDouble(
                strLength,
                "errorLength",
                "Length",
                errors
        );

        Double width = ValidateForm.parseDouble(
                strWidth,
                "errorWidth",
                "Width",
                errors
        );

        Boolean active = ValidateForm.parseBoolean(
                strActive,
                "errorActive",
                "Active",
                errors
        );

        if (!errors.isEmpty()) {

            return Result.fail(errors);
        } else {
            Hall hallCreateForm = new Hall(
                    hallName,
                    active,
                    width,
                    length,
                    height
            );
            return Result.ok(hallCreateForm);
        }

    }

    /**
     * Pagination ALL Halls
     * @param page
     * @param size
     * @return
     */
    public Result<Page<Hall>> getAllHallsPaged(int page, int size, Scope scope) {

        int pageNumber = Math.max(1, page);
        int pageSize = Math.max(1, size);
        int offset = (pageNumber - 1) * pageSize;

        Result<List<Hall>> content = scope == Scope.ALL?
                paginationGetAllHall(offset,pageSize):
                paginationGetAllActiveHall(offset, pageSize);
        if(!content.isSuccess()) {
            log.error(content.getErrors());
            return Result.fail(content.getErrors());
        }

        Result<Long> total = (scope == Scope.ALL)
                ? hallService.countAllHalls()
                : hallService.countActiveHalls();
        if(!total.isSuccess()) {
            log.error(content.getErrors());
            return Result.fail(total.getErrors());
        }

        // Création de la page
        Page<Hall> pageObj = Page.of(content.getData(), pageNumber, pageSize, total.getData());
        log.info("Pagination réussie[allHAlls] → " + pageObj);
        return Result.ok(pageObj);
    }

    /**
     * Retrieves HAll for PAgination
     * @param page
     * @param size
     * @return
     */
    private Result<List<Hall>> paginationGetAllHall(int page, int size) {
        return hallService.getAllHalls(page, size);
    }

    /**
     * Retrieves ActiveHall for PAgination
     * @param page
     * @param size
     * @return
     */
    private Result<List<Hall>> paginationGetAllActiveHall(int page, int size) {
        return hallService.getAllActiveHalls(page, size);
    }
}