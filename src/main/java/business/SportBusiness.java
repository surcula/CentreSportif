//package business;
//
//import Tools.ParamUtils;
//import Tools.Result;
//import dto.FieldUpdateForm;
//import dto.Page;
//import entities.Sport;
//import enums.Scope;
//import services.SportServiceImpl;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class SportBusiness {
//
//    private SportServiceImpl sportService;
//    // Log4j
//    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SportBusiness.class);
//
//    public SportBusiness(SportServiceImpl sportService) {
//        this.sportService = sportService;
//    }
//
//
//    /**
//     * validate field Form
//     * @param strActive
//     * @return
//     */
//    public static Result<Sport> initCreateForm(String strSportName, String strActive) {
//
//        Map<String, String> errors = new HashMap<>();
//
//        Result<String> fieldName = ValidateForm.stringIsEmpty(strFieldName, "errorFieldName", "Field name", errors);
//        if(!fieldName.isSuccess()) errors.putAll(fieldName.getErrors());
//
//        //Vérification de la taille du fieldName
//        Result<String> lengthFieldName = ValidateForm.stringLength(fieldName.getData(),0,255);
//        if(!lengthFieldName.isSuccess()) errors.putAll(lengthFieldName.getErrors());
//
//        //Vérification de l'id du hall [On ne va pas encore en DB le chercher]
//        Result<Integer> hallId = ParamUtils.verifyId(strHallId);
//        if(!hallId.isSuccess()) errors.putAll(hallId.getErrors());
//
//        //Vérification du boolean
//        Result<Boolean> active = ValidateForm.parseBoolean(strActive, "errorActive", "Active", errors);
//        if(!active.isSuccess()) errors.putAll(active.getErrors());
//
//        if(!errors.isEmpty()) {
//            return Result.fail(errors);
//        }
//
//        Result<Field> field = toEntity(fieldName.getData() ,active.getData());
//        return Result.ok(field.getData());
//    }
//
//    /**
//     * validate field Form
//     * @param strFieldName
//     * @param strHallId
//     * @param strActive
//     * @return
//     */
//    public static Result<FieldUpdateForm> initUpdateForm(String strFieldName, String strHallId, String strActive) {
//
//        Map<String, String> errors = new HashMap<>();
//
//        Result<String> fieldName = ValidateForm.stringIsEmpty(strFieldName, "errorFieldName", "Field name", errors);
//        if(!fieldName.isSuccess()) errors.putAll(fieldName.getErrors());
//
//        //Vérification de la taille du fieldName
//        Result<String> lengthFieldName = ValidateForm.stringLength(fieldName.getData(),0,255);
//        if(!lengthFieldName.isSuccess()) errors.putAll(lengthFieldName.getErrors());
//
//        //Vérification de l'id du hall [On ne va pas encore en DB le chercher]
//        Result<Integer> hallId = ParamUtils.verifyId(strHallId);
//        if(!hallId.isSuccess()) errors.putAll(hallId.getErrors());
//
//        //Vérification du boolean
//        Result<Boolean> active = ValidateForm.parseBoolean(strActive, "errorActive", "Active", errors);
//        if(!active.isSuccess()) errors.putAll(active.getErrors());
//
//        if(!errors.isEmpty()) {
//            return Result.fail(errors);
//        }
//
//        Result<FieldUpdateForm> fieldUpdateForm = toUpdateForm(fieldName.getData(), hallId.getData() ,active.getData());
//        return Result.ok(fieldUpdateForm.getData());
//    }
//
//
//
//    /**
//     * Pagination ALL Fields
//     * @param page
//     * @param size
//     * @return
//     */
//    public Result<Page<Field>> getAllFieldsPaged(int page, int size, Scope scope) {
//
//        int pageNumber = Math.max(1, page);
//        int pageSize = Math.max(1, size);
//        int offset = (pageNumber - 1) * pageSize;
//
//        Result<List<Field>> content = scope == Scope.ALL?
//                paginationGetAllField(offset,pageSize):
//                paginationGetAllActiveField(offset, pageSize);
//        if(!content.isSuccess()) {
//            log.error(content.getErrors());
//            return Result.fail(content.getErrors());
//        }
//
//        Result<Long> total = (scope == Scope.ALL)
//                ? fieldService.countAllFields()
//                : fieldService.countAllActiveFields();
//        if(!total.isSuccess()) {
//            log.error(content.getErrors());
//            return Result.fail(total.getErrors());
//        }
//
//        // Création de la page
//        Page<Field> pageObj = Page.of(content.getData(), pageNumber, pageSize, total.getData());
//        log.info("Pagination réussie[allFIELDs] → " + pageObj);
//        return Result.ok(pageObj);
//    }
//
//    /**
//     * Retrieves Field for PAgination
//     * @param page
//     * @param size
//     * @return
//     */
//    private Result<List<Field>> paginationGetAllField(int page, int size) {
//        return fieldService.getAllFields(page, size);
//    }
//
//    /**
//     * Retrieves ActiveField for PAgination
//     * @param page
//     * @param size
//     * @return
//     */
//    private Result<List<Field>> paginationGetAllActiveField(int page, int size) {
//        return fieldService.getAllActiveFields(page, size);
//    }
//
//    /**
//     * CreateField
//     * @param strFieldName
//     * @param active
//     * @return
//     */
//    private static Result<Field> toEntity(String strFieldName, boolean active){
//        Field field = new Field();
//        field.setFieldName(strFieldName);
//        field.setActive(active);
//        return Result.ok(field);
//    }
//
//    /**
//     * UpdateFieldForm
//     * @param strFieldName
//     * @param active
//     * @return
//     */
//    private static Result<FieldUpdateForm> toUpdateForm(String strFieldName, int hallId, boolean active){
//        FieldUpdateForm fieldUpdateForm = new FieldUpdateForm();
//        fieldUpdateForm.setFieldName(strFieldName);
//        fieldUpdateForm.setActive(active);
//        fieldUpdateForm.setHallId(hallId);
//        return Result.ok(fieldUpdateForm);
//    }
//
//}
