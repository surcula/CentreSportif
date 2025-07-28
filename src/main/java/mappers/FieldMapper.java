package mappers;

import dto.FieldCreateForm;
import entities.Field;

public class FieldMapper {
    /**
     * FieldCreateForm to field
     * @param fieldCreateForm
     * @return
     */
    public static Field fromCreateForm(FieldCreateForm fieldCreateForm) {
        Field field = new Field();
        field.setFieldName(fieldCreateForm.getFieldName());
        field.setHall(fieldCreateForm.getHall());
        field.setActive(fieldCreateForm.isActive());
        return field;
    }

}
