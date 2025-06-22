package business;

import dto.SportUpdateForm;
import entities.Sport;

public class SportBusiness {


    /**
     * Update Sport
     * @param sport
     * @param sportUpdateForm
     */
    public static void fromUpdateForm(Sport sport, SportUpdateForm sportUpdateForm) {
        sport.setSportName(sportUpdateForm.getSportName());
        sport.setActive(sportUpdateForm.isActive());
        sport.setPrice(sportUpdateForm.getPrice());
    }
}
