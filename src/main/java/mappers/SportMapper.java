package mappers;

import dto.SportCreateForm;
import entities.Sport;

public class SportMapper {

    /**
     * SportCreateForm to Sport
     * @param sportCreateForm
     * @return
     */
    public static Sport fromCreateForm(SportCreateForm sportCreateForm) {
        Sport sport = new Sport();
        sport.setSportName(sportCreateForm.getSportName());
        sport.setActive(sportCreateForm.isActive());
        sport.setPrice(sportCreateForm.getPrice());
        return sport;
    }
}
