package app.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "appointments")
public class Appointment {
    public Appointment(String vaccine,
                       String dose,
                       String date,
                       String time,
                       Venue venue) {
        this.vaccine = vaccine;
        this.dose = dose;
        this.date = date;
        this.time = time;
        this.venue_id = venue;
    }

    @Id
    @GeneratedValue
    private Long apt_id;
    @NotBlank
    private String vaccine;
    @NotBlank
    private String dose;
    @NotBlank
    private String date;
    @NotBlank
    private String time;

//    @NotBlank
    @OneToOne
    @JoinColumn(name = "venue_id")
    private Venue venue_id;

    @OneToOne(mappedBy = "apt_id")
    private User user;

    public Appointment() {}

    public Long getApt_id() {
        return apt_id;
    }

    public void setApt_id(Long aptId) {
        this.apt_id = aptId;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Venue getVenue() {
        return venue_id;
    }

    public void setVenue(Venue venue) {
        this.venue_id = venue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
