package business;

import Tools.Result;
import dto.HallUpdateForm;
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

        Result<String> hallName = ValidateForm.stringIsEmpty(strHallName, "errorHallName", "HallName", errors);
        if (!hallName.isSuccess()) errors.putAll(hallName.getErrors());

        Result<String> hallNameLength = ValidateForm.stringLength(strHallName, 0, 255);
        if (!hallNameLength.isSuccess()) errors.putAll(hallNameLength.getErrors());

        Result<Double> height = ValidateForm.parseDouble(strHeight, "errorHeight", "Height", errors);
        if (!height.isSuccess()) errors.putAll(height.getErrors());

        Result<Double> length = ValidateForm.parseDouble(strLength, "errorLength", "Length", errors);
        if (!length.isSuccess()) errors.putAll(length.getErrors());

        Result<Double> width = ValidateForm.parseDouble(strWidth, "errorWidth", "Width", errors);
        if (!width.isSuccess()) errors.putAll(width.getErrors());

        Result<Boolean> active = ValidateForm.parseBoolean(strActive, "errorActive", "Active", errors);
        if (!active.isSuccess()) errors.putAll(active.getErrors());

        if (!errors.isEmpty()) {
            return Result.fail(errors);
        } else {
            Result<Hall> hallCreateForm = toEntity(hallName.getData(), width.getData(), length.getData(), height.getData(), active.getData());
            return Result.ok(hallCreateForm.getData());
        }

    }

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
    public static Result<HallUpdateForm> initUpdateForm(String strHallName, String strWidth, String strLength, String strHeight, String strActive) {
        Map<String, String> errors = new HashMap<>();

        Result<String> hallName = ValidateForm.stringIsEmpty(strHallName, "errorHallName", "HallName", errors);
        if (!hallName.isSuccess()) errors.putAll(hallName.getErrors());

        Result<String> hallNameLength = ValidateForm.stringLength(strHallName, 0, 255);
        if (!hallNameLength.isSuccess()) errors.putAll(hallNameLength.getErrors());

        Result<Double> height = ValidateForm.parseDouble(strHeight, "errorHeight", "Height", errors);
        if (!height.isSuccess()) errors.putAll(height.getErrors());

        Result<Double> length = ValidateForm.parseDouble(strLength, "errorLength", "Length", errors);
        if (!length.isSuccess()) errors.putAll(length.getErrors());

        Result<Double> width = ValidateForm.parseDouble(strWidth, "errorWidth", "Width", errors);
        if (!width.isSuccess()) errors.putAll(width.getErrors());

        Result<Boolean> active = ValidateForm.parseBoolean(strActive, "errorActive", "Active", errors);
        if (!active.isSuccess()) errors.putAll(active.getErrors());

        if (!errors.isEmpty()) {
            return Result.fail(errors);
        } else {
            Result<HallUpdateForm> hallUpdateForm = toUpdateForm(hallName.getData(), width.getData(), length.getData(), height.getData(), active.getData());
            return Result.ok(hallUpdateForm.getData());
        }

    }


    /**
     * Pagination ALL Halls
     *
     * @param page
     * @param size
     * @return
     */
    public Result<Page<Hall>> getAllHallsPaged(int page, int size, Scope scope) {

        int pageNumber = Math.max(1, page);
        int pageSize = Math.max(1, size);
        int offset = (pageNumber - 1) * pageSize;

        Result<List<Hall>> content = scope == Scope.ALL ?
                paginationGetAllHall(offset, pageSize) :
                paginationGetAllActiveHall(offset, pageSize);
        if (!content.isSuccess()) {
            log.error(content.getErrors());
            return Result.fail(content.getErrors());
        }

        Result<Long> total = (scope == Scope.ALL)
                ? hallService.countAllHalls()
                : hallService.countActiveHalls();
        if (!total.isSuccess()) {
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
     *
     * @param page
     * @param size
     * @return
     */
    private Result<List<Hall>> paginationGetAllHall(int page, int size) {
        return hallService.getAllHalls(page, size);
    }

    /**
     * Retrieves ActiveHall for PAgination
     *
     * @param page
     * @param size
     * @return
     */
    private Result<List<Hall>> paginationGetAllActiveHall(int page, int size) {
        return hallService.getAllActiveHalls(page, size);
    }

    /**
     * create hall with form params validates
     * @return
     */
    private static Result<Hall> toEntity(String hallName, double width, double length, double height, boolean active){
        Hall hall = new Hall();
        hall.setHallName(hallName);
        hall.setWidth(width);
        hall.setLength(length);
        hall.setHeight(height);
        hall.setActive(active);
        return Result.ok(hall);
    }

    /**
     * create hall with form params validates
     * @return
     */
    private static Result<HallUpdateForm> toUpdateForm(String hallName, double width, double length, double height, boolean active){
        HallUpdateForm hallUpdateForm = new HallUpdateForm();
        hallUpdateForm.setHallName(hallName);
        hallUpdateForm.setWidth(width);
        hallUpdateForm.setLength(length);
        hallUpdateForm.setHeight(height);
        hallUpdateForm.setActive(active);
        return Result.ok(hallUpdateForm);
    }
}