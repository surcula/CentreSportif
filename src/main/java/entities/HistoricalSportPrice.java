package entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "historicals_sports_prices")
public class HistoricalSportPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "begin_date", nullable = false)
    private LocalDate beginDate;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private double price;

    @ManyToOne
    @JoinColumn(name = "sport_id")
    private Sport sport;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

}