package interfaces;

import Tools.Result;
import dto.Page;
import entities.Event;
import java.util.List;

/**
 * Service interface for managing Event entities
 * It contains the contract operations in relation with create, update, delete and retrieve events
 */
public interface EventService {
    /**
     * Create a new event
     * @param event the event entity to create
     * @return a {@link Result} indicating success or failure with error details
     */
    Result create(Event event);
    /**
     * Updates an existing event
     * @param event the event entity with updated data
     * @return a {@link Result} indicating success or failure with error details
     */
    Result update(Event event);

    /**
     * Delete an event in putting the status to inactive
     * @param event the event entity to delete
     * @return a {@link Result} indicating success or failure with error details
     */
    Result delete(Event event);

    /**
     * Search an id by its ID
     * @param id the identifier of the event
     * @return a {@link Result} containing the event if found, or errors otherwise
     */
    Result<Event> getOneById(int id);

    /**
     * Search all the events which are active with pagination
     * @param page the page number (starting at 1)
     * @param size the number of events per page
     * @return a {@link Result} containing the list of active events
     */
    Result<List<Event>> getAllActiveEvents(int page, int size);

    /**
     * Search all events
     * @param page the page number (starting at 1)
     * @param size the number of events per page
     * @return a list of all events
     */
    Result<Page<Event>> getAllEvents(int page, int size);

    /**
     * Method to search all events with a list to call in EventBusiness
     * @param page the number of the page
     * @param size the size of the items
     */
    Result<List<Event>> getAllEvents2(int page, int size);

    /**
     * count all active events
     * @return a list of all active events
     */
    Result<Long> countActiveEvents();
    /**
     * count all events
     * @return a liste of all events
     */
    Result<Long> countAllEvents();
}
