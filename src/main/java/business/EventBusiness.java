package business;

import dto.Page;
import entities.Event;
import Tools.Result;
import enums.Scope;
import services.EventServiceImpl;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
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

    private EntityManager em;
    private EventServiceImpl eventService;

    /**
     * Constructor
     * @param eventService
     */
    public EventBusiness(EventServiceImpl eventService, EntityManager em) {
        this.eventService = eventService;
        this.em = em;
    }


    // Log4j
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EventBusiness.class);
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
            event.setEventName(name.trim());
        }
        //Validation et converson des dates et heures

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        //Date de début
        if(startDateStr == null || startDateStr.trim().isEmpty()) {
            errors.put("startDateHour", "Le date et heure de début sont obligatoires");
        }else {
            try {
                //CORRECTION
                startDate = LocalDateTime.parse(startDateStr, formatter);
                event.setBeginDateHour(startDate);
            } catch (DateTimeParseException e) {
                errors.put("startDateHour", "Format de date invalide (doit être yyyy-MM-ddTHH:mm).");
            }
        }
        //Date de fin
        if(endDateStr == null || endDateStr.trim().isEmpty()) {
            errors.put("endDateHour", "Le date et heure de fin sont obligatoires");
        }else {
            try {
                //CORRECTION
                endDate = LocalDateTime.parse(endDateStr, formatter);
                event.setEndDateHour(endDate);
            } catch (DateTimeParseException e) {
                errors.put("endDateHour", "Format de date invalide (doit être yyyy-MM-ddTHH:mm).");
            }
        }
        //validation que la date de début est antérieure ou égale à la date de fin
        if(startDate != null && endDate != null && startDate.isAfter(endDate)) {
            errors.put("endDateHour", "La date de fin ne peut pas être antérieure à la date de début.");
        }
        //Description
        if (description == null || description.trim().isEmpty()) {
            errors.put("description", "La description est obligatoire");
        } else {
            event.setInfo(description.trim());
        }
        //image
        if(image == null || image.trim().isEmpty()){
            errors.put("image", "L'image est obligatoire.");
        } else {
            event.setPicture(image);
        }
        //statut
        boolean isActive = "1".equals(status);
        event.setActive(isActive);
        if(errors.isEmpty()) {
            return Result.ok(event);
        } else {
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

        try {
            List<Event> events;

            if(scope == Scope.ALL) {
                System.out.println(">>>>>> [ADMIN] Récupération de tous les évènements (actifs + inactifs) ");
                events = em.createNamedQuery("Event.getAll", Event.class)
                        .setFirstResult(offset)
                        .setMaxResults(pageSize)
                        .getResultList();
            }
            else {
                System.out.println(">>>>>>> [USER] Récupération uniquement des évènements actifs");
                events = em.createNamedQuery("Event.getAllActive", Event.class)
                        .setFirstResult(offset)
                        .setMaxResults(pageSize)
                        .getResultList();
            }
            // Log de debug
            System.out.println((">>>>> Nombre d'évènement récupérés : " + events.size()));
            //Total d'éléments pour la pagination
            Long totalElements = (scope == Scope.ALL)
                    ? em.createQuery("SELECT COUNT(e) FROM Event e", Long.class).getSingleResult()
                    : em.createQuery("SELECT COUNT(e) FROM Event e WHERE E.active = true", Long.class).getSingleResult();
            //Création de la page
            Page<Event> pageData = Page.of(events, pageNumber, pageSize, totalElements);

            return Result.ok(pageData);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errors = new HashMap<>();
            errors.put("Erreur","Erreur lors de la récupération des évènements : " + e.getMessage());
            return Result.fail(errors);
        }
    }


}
