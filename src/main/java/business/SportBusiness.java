package business;

import Tools.ParamUtils;
import Tools.Result;
import dto.Page;
import dto.SportUpdateForm;
import entities.Sport;
import enums.Scope;
import services.SportServiceImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SportBusiness {

    private SportServiceImpl sportService;
    // Log4j
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SportBusiness.class);

    public SportBusiness(SportServiceImpl sportService) {
        this.sportService = sportService;
    }


    /**
     * validate create form
     * @param strSportName
     * @param strPrice
     * @param strSessionDuration
     * @param strActive
     * @return
     */
    public static Result<Sport> initCreateForm(String strSportName, String strPrice, String strSessionDuration, String strActive) {

        Map<String, String> errors = new HashMap<>();

        Result<String> sportName = ValidateForm.stringIsEmpty(strSportName, "errorSportName", "Sport name", errors);
        if(!sportName.isSuccess()) errors.putAll(sportName.getErrors());

        //Vérification de la taille du sportName
        Result<String> lengthSportName = ValidateForm.stringLength(sportName.getData(),0,255);
        if(!lengthSportName.isSuccess()) errors.putAll(lengthSportName.getErrors());

        //Vérification du Parse de la sessionDuration en integer
        Result<Integer> sessionDuration = ParamUtils.stringToInteger(strSessionDuration);
        if(!sessionDuration.isSuccess()) errors.putAll(sessionDuration.getErrors());

        //Vérification du Parse de la price en integer
        Result<Double> price = ValidateForm.parseDouble(strPrice, "errorPrice", "Price", errors);
        if(!price.isSuccess()) errors.putAll(price.getErrors());

        //Vérification du boolean
        Result<Boolean> active = ValidateForm.parseBoolean(strActive, "errorActive", "Active", errors);
        if(!active.isSuccess()) errors.putAll(active.getErrors());

        if(!errors.isEmpty()) {
            return Result.fail(errors);
        }

        Result<Sport> Sport = toEntity(sportName.getData(), price.getData(), sessionDuration.getData() ,active.getData());
        return Result.ok(Sport.getData());
    }

    /**
     * validate update form
     * @param strSportName
     * @param strPrice
     * @param strSessionDuration
     * @param strActive
     * @return
     */
    public static Result<SportUpdateForm> initUpdateForm(String strSportName, String strPrice, String strSessionDuration, String strActive) {

        Map<String, String> errors = new HashMap<>();

        //Vérification du sportName
        Result<String> sportName = ValidateForm.stringIsEmpty(strSportName, "errorSportName", "Sport name", errors);
        if(!sportName.isSuccess()) errors.putAll(sportName.getErrors());

        //Vérification de la taille du sportName
        Result<String> lengthSportName = ValidateForm.stringLength(sportName.getData(),0,255);
        if(!lengthSportName.isSuccess()) errors.putAll(lengthSportName.getErrors());

        //Vérification du Parse Price
        Result<Double> price = ValidateForm.parseDouble(strPrice, "errorPrice", "Price", errors);
        if(!price.isSuccess()) errors.putAll(price.getErrors());

        //Vérification du parse de la durée de session
        Result<Integer> sessionDuration = ValidateForm.stringToInteger(strSessionDuration,"errorSessionDuration", "Session Duration", errors);
        if(!sessionDuration.isSuccess()) errors.putAll(sessionDuration.getErrors());

        //Vérification du boolean
        Result<Boolean> active = ValidateForm.parseBoolean(strActive, "errorActive", "Active", errors);
        if(!active.isSuccess()) errors.putAll(active.getErrors());

        if(!errors.isEmpty()) {
            return Result.fail(errors);
        }

        Result<SportUpdateForm> sportUpdateForm = toUpdateForm(sportName.getData(), price.getData() ,sessionDuration.getData(), active.getData());
        return Result.ok(sportUpdateForm.getData());
    }



    /**
     * Pagination ALL Sports
     * @param page
     * @param size
     * @return
     */
    public Result<Page<Sport>> getAllSportsPaged(int page, int size, Scope scope) {

        int pageNumber = Math.max(1, page);
        int pageSize = Math.max(1, size);
        int offset = (pageNumber - 1) * pageSize;

        Result<List<Sport>> content = scope == Scope.ALL?
                paginationGetAllSport(offset,pageSize):
                paginationGetAllActiveSport(offset, pageSize);
        if(!content.isSuccess()) {
            log.error(content.getErrors());
            return Result.fail(content.getErrors());
        }

        Result<Long> total = (scope == Scope.ALL)
                ? sportService.countActiveSports()
                : sportService.countAllSports();
        if(!total.isSuccess()) {
            log.error(content.getErrors());
            return Result.fail(total.getErrors());
        }

        // Création de la page
        Page<Sport> pageObj = Page.of(content.getData(), pageNumber, pageSize, total.getData());
        log.info("Pagination réussie[allSpors] → " + pageObj);
        return Result.ok(pageObj);
    }

    /**
     * Retrieves Sport for Pagination
     * @param page
     * @param size
     * @return
     */
    private Result<List<Sport>> paginationGetAllSport(int page, int size) {
        return sportService.getAllSports(page, size);
    }

    /**
     * Retrieves ActiveSport for Pagination
     * @param page
     * @param size
     * @return
     */
    private Result<List<Sport>> paginationGetAllActiveSport(int page, int size) {
        return sportService.getAllActiveSports(page, size);
    }

    /**
     * create a sport object with params
     * @param strSportName
     * @param price
     * @param sessionDuration
     * @param active
     * @return
     */
    private static Result<Sport> toEntity(String strSportName, double price, int sessionDuration ,boolean active){
        Sport sport = new Sport();
        sport.setSportName(strSportName);
        sport.setPrice(price);
        sport.setSessionDuration(sessionDuration);
        sport.setActive(active);
        return Result.ok(sport);
    }

    /**
     * create a sportUpdateForm object with params
     * @param strSportName
     * @param price
     * @param sessionDuration
     * @param active
     * @return
     */
    private static Result<SportUpdateForm> toUpdateForm(String strSportName, double price, int sessionDuration , boolean active){
        SportUpdateForm sportUpdateForm = new SportUpdateForm();
        sportUpdateForm.setSportName(strSportName);
        sportUpdateForm.setPrice(price);
        sportUpdateForm.setSessionDuration(sessionDuration);
        sportUpdateForm.setActive(active);
        return Result.ok(sportUpdateForm);
    }


}
