package business;

import Tools.Result;
import dto.HallCreateForm;
import dto.HallUpdateForm;
import entities.Hall;

import java.util.HashMap;
import java.util.Map;

public class HallBusiness {


    /**
     * Update Hall
     *
     * @param hall
     * @param hallUpdateForm
     */
    public static void fromUpdateForm(Hall hall, HallUpdateForm hallUpdateForm) {
        hall.setHallName(hallUpdateForm.getHallName());
        hall.setActive(hallUpdateForm.isActive());
    }

    /**
     * validate data submitted by CreateForm
     *
     * @param strHallName
     * @param strWidth
     * @param strLength
     * @param strHeight
     * @param strActive
     * @return
     */
    public static Result<HallCreateForm> initCreateForm(String strHallName, String strWidth, String strLength, String strHeight, String strActive) {
        Map<String, String> errors = new HashMap<>();

        String hallName = ValidateForm.stringIsEmpty(
                strHallName,
                "errorHallName",
                "Hall name",
                errors
        );

        Double height = ValidateForm.parseDouble(
                strHeight,
                "errorHeight",
                "Height",
                errors
        );

        Double length = ValidateForm.parseDouble(
                strLength,
                "errorLength",
                "Length",
                errors
        );

        Double width = ValidateForm.parseDouble(
                strWidth,
                "errorWidth",
                "Width",
                errors
        );

        Boolean active = ValidateForm.parseBoolean(
                strActive,
                "errorActive",
                "Active",
                errors
        );

        if (!errors.isEmpty()) {

            return Result.fail(errors);
        } else {
            HallCreateForm hallCreateForm = new HallCreateForm(
                    hallName,
                    active,
                    width,
                    length,
                    height
            );
            return Result.ok(hallCreateForm);
        }

    }

}
