package app;

import javax.persistence.Entity;
import javax.persistence.Id;

// this class is a combination of data from both user and appointment
// to help return joined data between these tables
@Entity
public class UserAptDetails {
    private String name;
    private String surname;
    @Id
    private Long id;
    private Long apt_id;
    private String venue;

    @Override
    public String toString() {
        return "UserAptDetails [name=" + name + ", " +
                "surname=" + surname + ", " +
                "id= " + id + ", " +
                "apt_id=" + apt_id + ", " +
                "apt_location" + venue + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApt_id() {
        return apt_id;
    }

    public void setApt_id(Long apt_id) {
        this.apt_id = apt_id;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String apt_venue) {
        this.venue = apt_venue;
    }
}
