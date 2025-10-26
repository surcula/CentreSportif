package entities;

import javax.persistence.*;


@NamedQueries({
        @NamedQuery(
                name = "Discount.findByName",
                query = "SELECT d FROM Discount d WHERE d.discountName = :n"
        ),
        @NamedQuery(
                name = "Discount.findActiveByName",
                query = "SELECT d FROM Discount d WHERE d.discountName = :n AND d.active = true"
        ),
        @NamedQuery(
                name = "Discount.findAllActive",
                query = "SELECT d FROM Discount d WHERE d.active = true"
        ),
        @NamedQuery(
                name = "Discount.findActiveIdByName",
                query = "SELECT d.id FROM Discount d WHERE d.discountName = :n AND d.active = true"
        )
})
@Entity
@Table(name = "discounts")
public class Discount {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "discount_name", nullable = false)
    private String discountName;

    @Column(name = "percent", nullable = false, precision = 15, scale = 2)
    private double percent;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}