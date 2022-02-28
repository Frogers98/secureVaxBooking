package app.model;

import javax.persistence.*;

@Entity
@Table(name = "venues")
public class Venue {

    @Id
    @GeneratedValue
    private Long venue_id;

    @Column(nullable = false, unique = true, length = 45)
    private String venue_name;

    private String venue_address;

    @OneToOne(mappedBy = "venue_id")
    private Appointment appointment;

    public Venue(String venue_name, String venue_address) {
        this.venue_name = venue_name;
        this.venue_address = venue_address;
    }

    // empty default constructor
    public Venue() {}

    public Long getId() {
        return venue_id;
    }

    public void setId(Long id) {
        this.venue_id = id;
    }

    public String getVenue_name() {
        return venue_name;
    }

    public void setVenue_name(String county) {
        this.venue_name = county;
    }
}
