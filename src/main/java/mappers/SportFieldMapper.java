package mappers;

import dto.SportFieldUpdateForm;
import entities.SportField;

public class SportFieldMapper {

    /**
     * Mapping sportUpdateForm to sportField
     * @param sportFieldUpdateForm
     * @return
     */
    public static SportField fromCreateForm(SportFieldUpdateForm sportFieldUpdateForm) {
        SportField sportField = new SportField();
        sportField.setId(sportFieldUpdateForm.getId());
        sportField.setField(sportFieldUpdateForm.getField());
        sportField.setSport(sportFieldUpdateForm.getSport());
        sportField.setDay(sportFieldUpdateForm.getDay());
        sportField.setDateStart(sportFieldUpdateForm.getDateStart());
        sportField.setStartTime(sportFieldUpdateForm.getStartTime());
        sportField.setEndTime(sportFieldUpdateForm.getEndTime());
        sportField.setActive(sportFieldUpdateForm.isActive());
        return sportField;
    }

}
