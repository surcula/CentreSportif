package business;

import dto.HallUpdateForm;
import entities.Hall;

public class HallBusiness {


    /**
     * Update Hall
     * @param hall
     * @param hallUpdateForm
     */
    public static void fromUpdateForm(Hall hall, HallUpdateForm hallUpdateForm) {
        hall.setHallName(hallUpdateForm.getHallName());
        hall.setActive(hallUpdateForm.isActive());
    }
}
