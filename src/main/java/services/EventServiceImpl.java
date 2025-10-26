package services;

import Tools.Result;
import entities.Event;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import dto.EMF;
import dto.Page;
import interfaces.EventService;

/**
 * Implementation of the {@link EventService} interface
 * Provide concrete persistence operations for event entities
 * using the injected {@link javax.persistence.EntityManager}
 */
public class EventServiceImpl implements interfaces.EventService {

    private EntityManager em;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EventServiceImpl.class);

    /**
     * Constructor for an EventServiceImpl with the specified EntityManager
     * @param em the EntityManager to use for database operations
     */
    public EventServiceImpl(EntityManager em){
        this.em = em;
    }

    /**
     * Method to create a new Event.
     * @param event the new event created
     * @return Result.ok method
     */
    @Override
    public Result create(Event event) {
        em.persist(event);
        return Result.ok();
    }

    /**
     * Method to update an event exist yet.
     * @param event the event updated
     * @return Result.ok method
     */
    @Override
    public Result update(Event event) {
        em.merge(event);
        return Result.ok();
    }

    /**
     * Method to delete an event exist yet (it puts the active => inactive)
     * @param event the event entity to delete
     * @return Result.ok method
     */
    @Override
    public Result delete(Event event) {
        event.setActive(false);//it is a soft delete
        em.merge(event);
        return Result.ok();
    }

    /**
     * Method to get an event with his ID
     * @param id the identifier of the event
     * @return Result.ok method
     */
    @Override
    public Result getOneById(int id) {
        Event event = em.find(Event.class, id);
        if(event == null) {
            Map<String, String> errors = new HashMap<>();
            errors.put("Introuvable", "No event found with ID " + id);
            return Result.fail(errors);
        }
        return Result.ok(event);
    }

    /**
     * Method to get the list of all active event
     * @param page the page number (starting at 1)
     * @param size the number of events per page
     * @ return a {@link Result} containing the list of active events
     * @return result.ok method
     */
    @Override
    public Result<List<Event>> getAllActiveEvents(int page, int size) {
        List<Event> events = em.createNamedQuery("Event.getAllActive", Event.class)
                .setFirstResult((page-1) * size)
                .setMaxResults(size)
                .getResultList();
        return Result.ok(events);
    }

    /**
     * Method to get the list of all events
     * @param page the page number (starting at 1)
     * @param size the number of events per page
     * @return Result.ok method
     */
    @Override
    public Result<Page<Event>> getAllEvents(int page, int size) {
        em = EMF.getEM();
        try {
            //Vérification que les valeurs entrées sont valies (suite à un problème de negative start position
            if (page < 1) page = 1;
            if (size < 1) size = 10;
            //-------------------------------------------------------------
            int firstResult = (page - 1) * size;
            if(firstResult < 0){
                System.err.println("ERREUR GRAVE : firstResult est négatif. Valeur calculée : " + firstResult + " (page= " + page + ", size=" + size + ")");
                firstResult = 0;//réinitialisation pour empêcher l'exception.
            }
            System.out.println(">> Pagination : page = " + page + ", size = " + size );

            List<Event> events = em.createNamedQuery("Event.getAll", Event.class)
                    .setFirstResult(firstResult)
                    .setMaxResults(size)
                    .getResultList();
            System.out.println(">> Evènements récupérés : " + events.size() );
            for (Event e : events) {
                System.out.println("Event: " + e.getEventName() + " / Active :" + e.isActive());
            }
            Long totalElements = em.createQuery("Select count(e) from Event e", Long.class)
                    .getSingleResult();
            Page<Event> pageData = Page.of(events, page, size, totalElements);
            return Result.ok(pageData);
        }catch (Exception e){
            e.printStackTrace();
            Map<String, String> errors = new HashMap<>();
            errors.put("Introuvable", "erreur lors du chargement des évènements " + e.getMessage());
            return Result.fail(errors);
        }
    }

    /**
     * Second method to get the list of all events
     * @param page the number of the page
     * @param size the size of the items
     * @return
     */
    public Result<List<Event>> getAllEvents2(int page, int size){
        em = EMF.getEM();
        //EventService eventService = new EventServiceImpl(em);
        //Vérification que les valeurs entrées sont valies (suite à un problème de negative start position
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        //-------------------------------------------------------------
        try {
            //Vérification que les valeurs entrées sont valies (suite à un problème de negative start position
            if (page < 1) page = 1;
            if (size < 1) size = 10;
            //-------------------------------------------------------------
            int firstResult = (page - 1) * size;
            if(firstResult < 0){
                System.err.println("ERREUR GRAVE : firstResult est négatif. Valeur calculée : " + firstResult + " (page= " + page + ", size=" + size + ")");
                firstResult = 0;//réinitialisation pour empêcher l'exception.
            }
            System.out.println(">> Pagination : page = " + page + ", size = " + size );
            List<Event> events = em.createNamedQuery("Event.getAll", Event.class)
                    .setFirstResult(firstResult)
                    .setMaxResults(size)
                    .getResultList();
            System.out.println(">> Evènements récupérés : " + events.size() );
            return Result.ok(events);
        }catch (Exception e){
            e.printStackTrace();
            Map<String, String> errors = new HashMap<>();
            errors.put("Introuvable", "erreur lors du chargement des évènements " + e.getMessage());
            return Result.fail(errors);
        }
    }

    /**
     * Method to count the active events
     * @return result method
     */
    @Override
    public Result<Long> countActiveEvents() {
        Long countAllEvents = em.createNamedQuery("Event.countAllActive", Long.class).getSingleResult();
        log.info("getAllEvents : " + countAllEvents);
        return Result.ok(countAllEvents);

    }

    /**
     * Method to count all the events
     * @return result method
     */
    @Override
    public Result<Long> countAllEvents() {
        Long countAllEvents = em.createNamedQuery("Event.getAll", Long.class).getSingleResult();
        log.info("getAllEvents : " + countAllEvents);
        return Result.ok(countAllEvents);
    }
}
