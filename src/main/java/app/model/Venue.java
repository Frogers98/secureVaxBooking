package app.model;

import javax.persistence.*;

@Entity
@Table(name = "venues")
public class Venue {

    @Id
    @GeneratedValue
    private Long venue_id;

    @Column(nullable = false, unique = true, length = 45)
    private String county;

    public Venue(String county) {
        this.county = county;
    }

    // empty default constructor
    public Venue() {}

    public Long getId() {
        return venue_id;
    }

    public void setId(Long venue_id) {
        this.venue_id = venue_id;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    @Override
    public String toString() {
        return "Venue {" +
                "venueId=" + venue_id +
                ", county='" + county + '\'' +
                '}';
    }
}
