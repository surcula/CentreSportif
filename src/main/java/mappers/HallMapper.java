package mappers;

import dto.HallUpdateForm;
import entities.Hall;

public class HallMapper {

    /**
     * Update Hall
     * @param hallUpdateForm
     */
    public static void fromUpdateForm(HallUpdateForm hallUpdateForm, Hall hall) {
        hall.setHallName(hallUpdateForm.getHallName());
        hall.setHeight(hallUpdateForm.getHeight());
        hall.setLength(hallUpdateForm.getLength());
        hall.setWidth(hallUpdateForm.getWidth());
        hall.setActive(hallUpdateForm.isActive());
    }
}
