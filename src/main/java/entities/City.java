package entities;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(
                name = "getAllActiveCities",
                query = "SELECT c FROM City c WHERE c.active = true ORDER BY c.cityName"
        ),
        @NamedQuery(
                name = "getActiveCitiesByZip",
                query = "SELECT c FROM City c WHERE c.active = true AND c.zipCode = :zip ORDER BY c.cityName"
        )
})
@Entity
@Table(name = "cities")
public class City {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "city_name", nullable = false)
    private String cityName;

    @Column(name = "zip_code", nullable = false)
    private int zipCode;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}