package business;

import dto.ClosureUpdateForm;
import entities.Closure;

public class ClosureBusiness {


    /**
     * update the closure
     * @param closure the existing closure entity to update
     * @param closureUpdateForm the form containing the new value
     */
    public static void updateFromUpdateForm(Closure closure,ClosureUpdateForm closureUpdateForm){
        closure.setEndDate(closureUpdateForm.getEndDate());
        closure.setStartDate(closureUpdateForm.getStartDate());
        closure.setActive(closureUpdateForm.isActive());
        closure.setSportsField(closureUpdateForm.getSportField());
    }
}
