package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authorizations")
public class Authorization {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "authorizations_name", nullable = false)
    private String authorizationsName;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorizationsName() {
        return authorizationsName;
    }

    public void setAuthorizationsName(String authorizationsName) {
        this.authorizationsName = authorizationsName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}