package entities;

import javax.persistence.*;

/**
 * Named Query
 */
@NamedQueries({
        @NamedQuery(
                name = "getAllActiveHalls",
                query = "SELECT h FROM Hall h WHERE h.active = true"
        ),
        @NamedQuery(
                name = "getAllHalls",
                query = "SELECT h FROM Hall h"
        ),
        @NamedQuery(
                name = "countAllActiveHalls",
                query = "SELECT COUNT(h) FROM Hall h WHERE h.active = true"
        ),
        @NamedQuery(
                name = "countAllHalls",
                query = "SELECT COUNT(h) FROM Hall h"
        )
})


@Entity
@Table(name = "halls")
public class Hall {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "hall_name", nullable = false)
    private String hallName;

    @Column(name = "width", nullable = false, precision = 5, scale = 2)
    private double width;

    @Column(name = "length", nullable = false, precision = 5, scale = 2)
    private double length;

    @Column(name = "height", nullable = false, precision = 5, scale = 2)
    private double height;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Hall(String hallName, boolean active, Double width, Double length, Double height) {
        this.hallName = hallName;
        this.active = active;
        this.width = width;
        this.length = length;
        this.height = height;
    }
    public Hall() {}

}