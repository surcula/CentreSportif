package business;

import dto.Page;
import entities.Event;
import Tools.Result;
import enums.Scope;
import services.EventServiceImpl;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Class to handle event-related validation
 * and initilisation before database operations.
 * So this class validate the input from the event creation form and convert it into a valid {@link Event}
 */


public class EventBusiness {
    /**
     * Constructor
     * @param eventService
     */
    public EventBusiness(EventServiceImpl eventService) {
        this.eventService = eventService;
    }

    private EventServiceImpl eventService;
    // Log4j
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HallBusiness.class);
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

    /**
     * Method to get all the events page
     * @param page
     * @param size
     * @param scope
     * @return Result
     */
    public Result<Page<Event>> getAllEventsPaged(int page, int size, Scope scope) {

        int pageNumber = Math.max(1, page);
        int pageSize = Math.max(1, size);
        int offset = (pageNumber - 1) * pageSize;

        Result<List<Event>> content = scope == Scope.ALL?
                paginationGetAllEvent(offset,pageSize):
                paginationGetAllActiveEvent(offset, pageSize);
        if(!content.isSuccess()) {
            log.error(content.getErrors());
            return Result.fail(content.getErrors());
        }

        Result<Long> total = (scope == Scope.ALL)
                ? eventService.countAllEvents()
                : eventService.countActiveEvents();
        if(!total.isSuccess()) {
            log.error(content.getErrors());
            return Result.fail(total.getErrors());
        }

        // Création de la page
        Page<Event> pageObj = Page.of(content.getData(), pageNumber, pageSize, total.getData());
        log.info("Pagination réussie[allEvents] → " + pageObj);
        return Result.ok(pageObj);
    }

    /**
     * Retrieves Event for PAgination
     * @param page
     * @param size
     * @return
     */
    private Result<List<Event>> paginationGetAllEvent(int page, int size) {
        return eventService.getAllActiveEvents(page, size);
    }

    /**
     * Retrieves ActiveEvent for Pagination
     * @param page
     * @param size
     * @return
     */
    private Result<List<Event>> paginationGetAllActiveEvent(int page, int size) {
        return eventService.getAllEvents2(page, size);
    }

}
