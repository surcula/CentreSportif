package entities;

import javax.persistence.*;
import java.time.Instant;

/**
 * Named Query
 */
@NamedQueries({
        @NamedQuery(
                name = "getAllActiveSportFields",
                query = "SELECT sp FROM SportField sp WHERE sp.active = true order by sp.id asc"
        ),
        @NamedQuery(
                name = "getAllSportFields",
                query = "SELECT sp FROM SportField sp order by sp.id asc"
        ),
        @NamedQuery(
                name = "countAllActiveSportFields",
                query = "SELECT COUNT(sp) FROM SportField sp WHERE sp.active = true"
        ),
        @NamedQuery(
                name = "countAllSportFields",
                query = "SELECT COUNT(sp) FROM SportField sp"
        )
})

@Entity
@Table(name = "sports_fields")
public class SportField {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @Column(name = "date_start", nullable = false)
    private Instant dateStart;

    @Column(name = "day", nullable = false)
    private int day;

    //@Column(name = "price", nullable = false, precision = 10, scale = 2)
    @Transient
    private double price;

    //@Column(name = "session_duration", nullable = false)
    @Transient
    private int sessionDuration;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Instant getDateStart() {
        return dateStart;
    }

    public void setDateStart(Instant dateStart) {
        this.dateStart = dateStart;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(int sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}