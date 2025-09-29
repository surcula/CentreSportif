package entities;

import javax.persistence.*;


/**
 * Named Query
 */
@NamedQueries({
        @NamedQuery(
                name = "getAllActiveFields",
                query = "SELECT f FROM Field f WHERE f.active = true"
        ),
        @NamedQuery(
                name = "getAllFields",
                query = "SELECT f FROM Field f"
        ),
        @NamedQuery(
                name = "countAllActiveFields",
                query = "SELECT COUNT(f) FROM Field f WHERE f.active = true"
        ),
        @NamedQuery(
                name = "countAllFields",
                query = "SELECT COUNT(f) FROM Field f"
        )
})


@Entity
@Table(name = "fields")
public class Field {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "field_name", nullable = false)
    private String fieldName;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}