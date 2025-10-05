package entities;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Named Query
 */
@NamedQueries({
        @NamedQuery(
                name = "getAllActiveClosures",
                query = "SELECT c FROM Closure c WHERE c.active = true"
        ),
        @NamedQuery(
                name = "getAllClosures",
                query = "SELECT c FROM Closure c"
        ),
        @NamedQuery(
                name = "countAllActiveClosures",
                query = "SELECT COUNT(c) FROM Closure c WHERE c.active = true"
        ),
        @NamedQuery(
                name = "countAllClosures",
                query = "SELECT COUNT(c) FROM Closure c"
        )
})


@Entity
@Table(name = "closures")
public class Closure {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "sports_field_id")
    private SportField sportField;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public SportField getSportsField() {
        return sportField;
    }

    public void setSportsField(SportField sportField) {
        this.sportField = sportField;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}