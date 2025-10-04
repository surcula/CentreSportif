package entities;

import javax.persistence.*;


/**
 * Named Query
 */
@NamedQueries({
        @NamedQuery(
                name = "getAllActiveSports",
                query = "SELECT s FROM Sport s WHERE s.active = true"
        ),
        @NamedQuery(
                name = "getAllSports",
                query = "SELECT s FROM Sport s"
        ),
        @NamedQuery(
                name = "countAllActiveSports",
                query = "SELECT COUNT(s) FROM Sport s WHERE s.active = true"
        ),
        @NamedQuery(
                name = "countAllSports",
                query = "SELECT COUNT(s) FROM Sport s"
        )
})

@Entity
@Table(name = "sports")
public class Sport {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "sport_name", nullable = false)
    private String sportName;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private double price;

    @Column(name = "session_duration", nullable = false)
    private int sessionDuration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

}