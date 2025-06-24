package business;

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
     * @param hallName
     * @param strWidth
     * @param strLength
     * @param strHeight
     * @param strActive
     * @return
     */
    public static Map<String, String> initCreateForm(String hallName, String strWidth, String strLength, String strHeight, String strActive) {
        Map<String, String> errors = new HashMap<>();

        if (hallName == null || hallName.trim().isEmpty()) {
            errors.put("errorHallName", "Le nom est requis.");
        }

        Double width = null, length = null, height = null;

        ValidateForm.parseDouble(
                strHeight,
                "errorHeight",
                "Height",
                errors
        );

        ValidateForm.parseDouble(
                strLength,
                "errorLength",
                "Length",
                errors
        );

        ValidateForm.parseDouble(
                strWidth,
                "errorWidth",
                "Width",
                errors
        );

        if (strActive == null || (!strActive.equals("1") && !strActive.equals("0"))) {
            errors.put("errorActive", "Le statut actif est requis.");
        }

        if(errors.size() > 0) {

            HallCreateForm hallCreateForm = new HallCreateForm(
                    hallName,
                    "1".equals(strActive),
                    width,
                    length,
                    height
            );
            return errors;
        }else{
            return errors;
        }

    }

}
