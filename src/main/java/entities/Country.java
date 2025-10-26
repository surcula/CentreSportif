package entities;

import javax.persistence.*;

@NamedQuery(name = "getAllActiveCountries", query ="SELECT c FROM Country c WHERE c.active = true ORDER BY c.countryName" )

@Entity
@Table(name = "countries")
public class Country {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "country_name", nullable = false)
    private String countryName;

    @Column(name = "iso_alpha3", nullable = false)
    private String isoAlpha3;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getIsoAlpha3() {
        return isoAlpha3;
    }

    public void setIsoAlpha3(String isoAlpha3) {
        this.isoAlpha3 = isoAlpha3;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}