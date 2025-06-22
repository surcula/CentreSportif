package mappers;

import dto.SportFieldCreateForm;
import entities.SportField;

public class SportFieldMapper {

    /**
     * Mapping sportFieldCreateForm to sportField
     * @param sportFieldCreateForm
     * @return
     */
    public static SportField fromCreateForm(SportFieldCreateForm sportFieldCreateForm) {
        SportField sportField = new SportField();
        sportField.setField(sportFieldCreateForm.getField());
        sportField.setSport(sportFieldCreateForm.getSport());
        sportField.setDay(sportFieldCreateForm.getDay());
        sportField.setDateStart(sportFieldCreateForm.getDateStart());
        sportField.setStartTime(sportFieldCreateForm.getStartTime());
        sportField.setEndTime(sportFieldCreateForm.getEndTime());
        sportField.setActive(sportFieldCreateForm.isActive());
        return sportField;
    }

}
