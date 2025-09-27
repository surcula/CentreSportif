package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Event entity to save the different event.
 *
 */
@Entity
@Table(name = "events")
public class Event {
    /**
     * Id of the event
     * automatically generated
     * @param id
     */
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    /**
     * Name of the event
     * @param eventName
     */
    @Column(name = "event_name", nullable = false)
    private String eventName;
    /**
     * The starting date of the event
     * @param beginDateHour
     */
    @Column(name = "begin_date_hour", nullable = false)
    private LocalDate beginDateHour;
    /**
     * The end date of the event
     * @param endDateHour
     */
    @Column(name = "end_date_hour", nullable = false)
    private LocalDate endDateHour;
    /**
     * The description of the event
     * @param info
     *
     */
    @Column(name = "info", nullable = false)
    private String info;
    /**
     * The picture of the event
     * @param picture
     */
    @Column(name = "picture", nullable = false)
    private String picture;
    /**
     * The status of the event
     *
     */
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
    /**
     * The method to get the id of the event
     * @return id
     *
     */
    public int getId() {
        return id;
    }
    /**
     * The method to set the id of the event
     * @param id
     *
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * The method to get the name of the event
     * @return eventName
     *
     */
    public String getEventName() {
        return eventName;
    }
    /**
     * The method to set the name of the event
     * @param eventName
     *
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    /**
     * The method to get the start date of the event
     * @return beginDateHour
     *
     */
    public LocalDate getBeginDateHour() {
        return beginDateHour;
    }
    /**
     * The method to set the start date of the event
     * @param beginDateHour
     *
     */
    public void setBeginDateHour(LocalDate beginDateHour) {
        this.beginDateHour = beginDateHour;
    }
    /**
     * The method to get the end date of the event
     * @return endDateHour
     *
     */
    public LocalDate getEndDateHour() {
        return endDateHour;
    }
    /**
     * The method to set the end date of the event
     * @param endDateHour
     *
     */
    public void setEndDateHour(LocalDate endDateHour) {
        this.endDateHour = endDateHour;
    }
    /**
     * The method to get the information about the event
     * @return info
     *
     */
    public String getInfo() {
        return info;
    }
    /**
     * The method to set the information about the event
     * @param  info
     *
     */
    public void setInfo(String info) {
        this.info = info;
    }
    /**
     * The method to get the picture of the event
     * @return picture
     *
     */
    public String getPicture() {
        return picture;
    }
    /**
     * The method to set the picture of the event
     * @param picture
     *
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }
    /**
     * The method to get the status of the event
     * @return active
     *
     */
    public boolean isActive() {
        return active;
    }
    /**
     * The method to set the status of the event
     * @param active
     *
     */
    public void setActive(boolean active) {
        this.active = active;
    }

}