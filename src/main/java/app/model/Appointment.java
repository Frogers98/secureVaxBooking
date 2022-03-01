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
                       String venue) {
        this.vaccine = vaccine;
        this.dose = dose;
        this.date = date;
        this.time = time;
        this.venue = venue;
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
    @NotBlank
    private String venue;
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

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "apt_id=" + apt_id +
                ", vaccine='" + vaccine + '\'' +
                ", dose='" + dose + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", venue='" + venue + '\'' +
                ", user=" + user +
                '}';
    }
}
