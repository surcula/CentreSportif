package entities;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Named Query
 */
@NamedQueries({
        @NamedQuery(
                name = "getAllActiveSportFields",
                query = "SELECT sp FROM SportField sp WHERE sp.active = true order by sp.field.fieldName,sp.sport.sportName, sp.day asc"
        ),
        @NamedQuery(
                name = "getAllSportFields",
                query = "SELECT sp FROM SportField sp order by sp.field.fieldName,sp.sport.sportName, sp.day asc"
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
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "date_start", nullable = false)
    private LocalDateTime dateStart;

    @Column(name = "day", nullable = false)
    private int day;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private double price;

    @Column(name = "session_duration", nullable = false)
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDateTime dateStart) {
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

    /**
     * Return label for day
     * @return
     */
    @Transient
    public String getDayLabel() {
        switch (this.day) {
            case 1: return "Lundi";
            case 2: return "Mardi";
            case 3: return "Mercredi";
            case 4: return "Jeudi";
            case 5: return "Vendredi";
            case 6: return "Samedi";
            case 7: return "Dimanche";
            default: return "Inconnu";
        }
    }


}