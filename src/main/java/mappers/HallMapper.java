package mappers;

import dto.HallCreateForm;
import entities.Hall;

public class HallMapper {
    /**
     * Mapper HallCreateForm to Hall
     * @param hallCreateForm
     * @return
     */
    public static Hall fromCreateForm(HallCreateForm hallCreateForm) {
        Hall hall = new Hall();
        hall.setActive(hallCreateForm.isActive());
        hall.setHallName(hallCreateForm.getHallName());
        hall.setLength(hallCreateForm.getLength());
        hall.setWidth(hallCreateForm.getWidth());
        hall.setHeight(hallCreateForm.getHeight());
        return hall;
    }

}
