package mappers;

import dto.FieldUpdateForm;
import entities.Field;

public class FieldMapper {
    /**
     * Update field
     * @param fieldUpdateForm
     * @return
     */
    public static Field fromUpdateForm(FieldUpdateForm fieldUpdateForm) {
        Field field = new Field();
        field.setFieldName(fieldUpdateForm.getFieldName());
        field.setHall(fieldUpdateForm.getHall());
        field.setActive(fieldUpdateForm.isActive());
        return field;
    }

}
