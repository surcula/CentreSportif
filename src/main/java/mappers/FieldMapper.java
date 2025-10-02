package mappers;

import dto.FieldUpdateForm;
import entities.Field;
import entities.Hall;

public class FieldMapper {

    /**
     * Update field
     * @param fieldUpdateForm
     */
    public static void fromUpdateForm(FieldUpdateForm fieldUpdateForm, Hall hall, Field field) {
        field.setFieldName(fieldUpdateForm.getFieldName());
        field.setHall(hall);
        field.setActive(fieldUpdateForm.isActive());
    }

}
