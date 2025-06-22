package mappers;

import dto.ClosureCreateForm;
import entities.Closure;

public class ClosureMapper {

    /**
     * Mapping ClosureForm to Closure
     * @param closureCreateForm
     * @return
     */
    public static Closure fromCreateForm(ClosureCreateForm closureCreateForm){
        Closure closure = new Closure();
        closure.setEndDate(closureCreateForm.getEndDate());
        closure.setStartDate(closureCreateForm.getStartDate());
        closure.setActive(closureCreateForm.isActive());
        closure.setSportsField(closureCreateForm.getSportField());
        return  closure;
    }
}
