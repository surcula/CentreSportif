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
        return hall;
    }

}
