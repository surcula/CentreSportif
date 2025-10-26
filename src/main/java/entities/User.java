package entities;


import javax.persistence.Column;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import javax.persistence.*;
import java.time.LocalDate;



@Entity
@NamedQueries({
        // inscription / connexion
        @NamedQuery(name = "User.findByEmail",
                query = "SELECT u FROM User u WHERE u.email = :email"),
        @NamedQuery(name = "User.countByEmail",
                query = "SELECT COUNT(u) FROM User u WHERE u.email = :email"),
        @NamedQuery(name = "User.findByEmailWithRole",
                query = "SELECT u FROM User u LEFT JOIN FETCH u.role WHERE u.email = :email"),

        // listing simple
        @NamedQuery(name = "User.findAllOrdered",
                query = "SELECT u FROM User u ORDER BY u.lastName, u.firstName"),
        @NamedQuery(name = "User.searchQ",
                query = "SELECT u FROM User u " +
                        "WHERE (:q IS NULL OR :q = '' OR " +
                        "LOWER(u.lastName) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
                        "LOWER(u.firstName) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
                        "LOWER(u.email) LIKE LOWER(CONCAT('%',:q,'%'))) " +
                        "ORDER BY u.lastName, u.firstName"),
        @NamedQuery(name = "User.searchQStatus",
                query = "SELECT u FROM User u " +
                        "WHERE (:q IS NULL OR :q = '' OR " +
                        "LOWER(u.lastName) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
                        "LOWER(u.firstName) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
                        "LOWER(u.email) LIKE LOWER(CONCAT('%',:q,'%'))) " +
                        "AND (:status IS NULL OR u.active = :status) " +
                        "ORDER BY u.lastName, u.firstName"),
        @NamedQuery(name = "User.countQStatus",
                query = "SELECT COUNT(u) FROM User u " +
                        "WHERE (:q IS NULL OR :q = '' OR " +
                        "LOWER(u.lastName) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
                        "LOWER(u.firstName) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
                        "LOWER(u.email) LIKE LOWER(CONCAT('%',:q,'%'))) " +
                        "AND (:status IS NULL OR u.active = :status)"),


        // recherche + filtre + exclusions de rôles pour la gestion d'utilisateurs
        @NamedQuery(name = "User.searchQStatusExcl",
                query = "SELECT u FROM User u " +
                        "WHERE (:q IS NULL OR :q = '' OR " +
                        "LOWER(u.lastName) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
                        "LOWER(u.firstName) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
                        "LOWER(u.email) LIKE LOWER(CONCAT('%',:q,'%'))) " +
                        "AND (:status IS NULL OR u.active = :status) " +
                        "AND u.role.roleName NOT IN :excl " +
                        "ORDER BY u.lastName, u.firstName"),
        @NamedQuery(name = "User.countQStatusExcl",
                query = "SELECT COUNT(u) FROM User u " +
                        "WHERE (:q IS NULL OR :q = '' OR " +
                        "LOWER(u.lastName) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
                        "LOWER(u.firstName) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
                        "LOWER(u.email) LIKE LOWER(CONCAT('%',:q,'%'))) " +
                        "AND (:status IS NULL OR u.active = :status) " +
                        "AND u.role.roleName NOT IN :excl")
})

@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "first_name", nullable = false, length = 90)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 90)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;


    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Column(name = "phone", nullable = false)
    private String phone;


    @Column(name = "gender", length = 10, nullable = false)
    private String gender;


    @Column(name = "civilite", length = 10 ,nullable = false)
    private String civilite;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "event_id")
    private int eventId;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "is_blacklist", nullable = false)
    private Boolean blacklist = false;

    public Boolean isBlacklist() {
        return blacklist;
    }

    public Boolean getBlacklist() {
        return blacklist;
    }
    public void setBlacklist(Boolean blacklist) {
        this.blacklist = blacklist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}