package entities;

import javax.persistence.*; // <-- important pour @Entity, @Table, @Id, @Column, @NamedQuery(s)
import java.io.Serializable;

@NamedQueries({
        @NamedQuery(
                name = "Sport.getAllSports",
                query = "SELECT s FROM Sport s ORDER BY s.id ASC"
        ),
        @NamedQuery(
                name = "Sport.countAllActiveSports",
                query = "SELECT COUNT(s) FROM Sport s WHERE s.active = true"
        ),
        @NamedQuery(
                name = "Sport.countAllSports",
                query = "SELECT COUNT(s) FROM Sport s"
        ),
        @NamedQuery(
                name = "Sport.getUnitPriceById",
                query = "SELECT s.price FROM Sport s WHERE s.id = :sid"
        )
})
@Entity
@Table(name = "sports") // si ta table s'appelle autrement, adapte
public class Sport implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "sport_name", nullable = false)
    private String sportName;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    // ⚠️ nécessaire si tu utilises Sport.getUnitPriceById
    @Column(name = "price", nullable = false)
    private Double price;

    public Sport() {}

    // getters/setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSportName() { return sportName; }
    public void setSportName(String sportName) { this.sportName = sportName; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
