package mappers;

import dto.SportUpdateForm;
import entities.Sport;

public class SportMapper {

    /**
     * SportCreateForm to Sport
     * @param sportUpdateForm
     * @return
     */
    public static Sport fromCreateForm(SportUpdateForm sportUpdateForm) {
        Sport sport = new Sport();
        sport.setSportName(sportUpdateForm.getSportName());
        sport.setActive(sportUpdateForm.isActive());
        sport.setPrice(sportUpdateForm.getPrice());
        return sport;
    }
}
