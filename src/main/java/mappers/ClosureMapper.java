package mappers;

import dto.ClosureUpdateForm;
import entities.Closure;

public class ClosureMapper {

    /**
     * Mapping ClosureUpdateForm to Closure
     * @param closureUpdateForm
     * @return
     */
    public static Closure fromUpdateForm(ClosureUpdateForm closureUpdateForm){
        Closure closure = new Closure();
        closure.setId(closureUpdateForm.getId());
        closure.setEndDate(closureUpdateForm.getEndDate());
        closure.setStartDate(closureUpdateForm.getStartDate());
        closure.setActive(closureUpdateForm.isActive());
        closure.setSportsField(closureUpdateForm.getSportField());
        return  closure;
    }
}
