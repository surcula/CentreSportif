package business;

import Tools.ParamUtils;
import Tools.Result;
import dto.Page;
import entities.Field;
import enums.Scope;
import services.FieldServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldBusiness {

    private FieldServiceImpl fieldService;
    // Log4j
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FieldBusiness.class);

    public FieldBusiness(FieldServiceImpl fieldService) {
        this.fieldService = fieldService;
    }


    /**
     * validate field Form
     * @param strFieldName
     * @param strHallId
     * @param strActive
     * @return
     */
    public static Result<Field> initCreateForm(String strFieldName, String strHallId, String strActive) {
        Field field = new Field();

        Map<String, String> errors = new HashMap<>();

        String fieldName = ValidateForm.stringIsEmpty(
                strFieldName,
                "errorFieldName",
                "Field name",
                errors
        );
        //Vérification de la taille du fieldName
        Result<String> lengthFieldName = ValidateForm.stringLength(fieldName,0,255);
        if(!lengthFieldName.isSuccess()) errors.putAll(lengthFieldName.getErrors());

        //Vérification de l'id du hall [Normalement pas besoin d'aller en DB dans ce cas-ci]
        Result<Integer> idResult = ParamUtils.verifyId(strHallId);
        if(!idResult.isSuccess()) errors.putAll(idResult.getErrors());

        return Result.ok(field);
    }

    /**
     * Pagination ALL Fields
     * @param page
     * @param size
     * @return
     */
    public Result<Page<Field>> getAllFieldsPaged(int page, int size, Scope scope) {

        int pageNumber = Math.max(1, page);
        int pageSize = Math.max(1, size);
        int offset = (pageNumber - 1) * pageSize;

        Result<List<Field>> content = scope == Scope.ALL?
                paginationGetAllField(offset,pageSize):
                paginationGetAllActiveField(offset, pageSize);
        if(!content.isSuccess()) {
            log.error(content.getErrors());
            return Result.fail(content.getErrors());
        }

        Result<Long> total = (scope == Scope.ALL)
                ? fieldService.countAllFields()
                : fieldService.countAllActiveFields();
        if(!total.isSuccess()) {
            log.error(content.getErrors());
            return Result.fail(total.getErrors());
        }

        // Création de la page
        Page<Field> pageObj = Page.of(content.getData(), pageNumber, pageSize, total.getData());
        log.info("Pagination réussie[allFIELDs] → " + pageObj);
        return Result.ok(pageObj);
    }

    /**
     * Retrieves Field for PAgination
     * @param page
     * @param size
     * @return
     */
    private Result<List<Field>> paginationGetAllField(int page, int size) {
        return fieldService.getAllFields(page, size);
    }

    /**
     * Retrieves ActiveField for PAgination
     * @param page
     * @param size
     * @return
     */
    private Result<List<Field>> paginationGetAllActiveField(int page, int size) {
        return fieldService.getAllActiveFields(page, size);
    }

}
