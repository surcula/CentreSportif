package mappers;

import dto.HallCreateForm;
import dto.HallUpdateForm;
import entities.Hall;

public class HallMapper {

    /**
     * Update Hall
     *
     * @param hallUpdateForm
     */
    public static Hall fromUpdateForm(HallUpdateForm hallUpdateForm) {
        Hall hall = new Hall();
        hall.setId(hallUpdateForm.getId());
        hall.setHallName(hallUpdateForm.getHallName());
        hall.setHeight(hallUpdateForm.getHeight());
        hall.setLength(hallUpdateForm.getLength());
        hall.setWidth(hallUpdateForm.getWidth());
        hall.setActive(hallUpdateForm.isActive());
        return hall;
    }

}
