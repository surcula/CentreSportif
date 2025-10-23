package mappers;

import dto.SportUpdateForm;
import entities.Sport;

public class SportMapper {

    /**
     * Sportupdateform to Sport
     * @param sportUpdateForm
     * @return
     */
    public static void fromUpdateForm(SportUpdateForm sportUpdateForm, Sport sport) {
        sport.setSportName(sportUpdateForm.getSportName());
        sport.setActive(sportUpdateForm.isActive());
    }
}
