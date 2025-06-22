package business;

import dto.FieldUpdateForm;
import entities.Field;

public class FieldBusiness {

    /**
     * Update field
     * @param field
     * @param fieldUpdateForm
     */
    public static void fromUpdateForm(Field field, FieldUpdateForm fieldUpdateForm) {
        field.setFieldName(fieldUpdateForm.getFieldName());
        field.setHall(fieldUpdateForm.getHall());
        field.setActive(fieldUpdateForm.isActive());
    }
}
