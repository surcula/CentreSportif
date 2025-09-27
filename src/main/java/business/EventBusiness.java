package business;

import com.sun.javafx.collections.MappingChange;
import entities.Event;
import Tools.Result;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to handle event-related validation
 * and initilisation before database operations.
 * So this class validate the input from the event creation form and convert it into a valid {@link Event}
 */


public class EventBusiness {
    /**
     * Initialisation and validation of the data submitted via the Event creation Form
     * @param name the name of the event
     * @param startDateStr the start date and time in format "yyyy-MM-dd'T'HH:mm"
     * @param endDateStr the end date and time in format "yyyy-MM-dd'T'HH:mm"
     * @param description the description of the event (a little text)
     * @param image the image of the Event
     * @param status the status of the event, 1 for "En cours" and 0 for "Terminé"
     * @return a {@link Result} which contains a valid {@link Event} instance or an errors
     */
    public static Result<Event> initCreateForm(String name, String startDateStr, String endDateStr,
                                               String description, String image, String status ){
        //List<String> errors = new ArrayList<>();
        Map<String, String> errors = new HashMap<>();
        Event event = new Event();

        //validation du nom
        if(name == null || name.trim().isEmpty()) {
            errors.put("name", "Le nom est obligatoire");
        } else {
            event.setEventName(name);
        }
        //Validation des dates

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        try {
            event.setBeginDateHour(LocalDate.parse(startDateStr, formatter));
        }catch (DateTimeParseException e){
            errors.put("startDateHour", "Format de date invalide");
        }
        try {
            event.setEndDateHour(LocalDate.parse(endDateStr, formatter));
        }catch (DateTimeParseException e){
            errors.put("endDateHour", "Format de date invalide");
        }

        event.setInfo(description);
        event.setPicture(image);

        boolean isActive = "1".equals(status);//1 = en cours
        event.setActive(isActive);

        if(errors.isEmpty()) {
            return Result.ok(event);
        }else {
            return Result.fail(errors);
        }
    }

}
