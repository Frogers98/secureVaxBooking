package app.model;

import javax.persistence.*;

@Entity
@Table(name = "venues")
public class Venue {

    @Id
    @Column(name = "venue_id")
    private Long venue_id;

    @Column(nullable = false, unique = true, length = 45)
    private String venue_name;

    private String venue_address;

//    @JoinColumn("venue_id")
//    private Appointment appointment;

    public Venue(Long venue_id, String venue_name, String venue_address) {
        this.venue_id = venue_id;
        this.venue_name = venue_name;
        this.venue_address = venue_address;
    }

    // empty default constructor
    public Venue() {}

    public Long getId() {
        return venue_id;
    }

    public void setId(Long venue_id) {
        this.venue_id = venue_id;
    }

    public String getVenue_name() {
        return venue_name;
    }

    public void setVenue_name(String venue_name) {
        this.venue_name = venue_name;
    }

    @Override
    public String toString() {
        return "Venue {" +
                "venueId=" + venue_id +
                ", county='" + venue_name + '\'' +
                '}';
    }
}
