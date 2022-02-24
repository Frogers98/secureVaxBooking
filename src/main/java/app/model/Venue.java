package app.model;

import javax.persistence.*;

@Entity
@Table(name = "venues")
public class Venue {

    @Id
    @GeneratedValue
    private Long venueId;

    @Column(nullable = false, unique = true, length = 45)
    private String county;

    public Venue(String county) {
        this.county = county;
    }

    // empty default constructor
    public Venue() {}

    public Long getId() {
        return venueId;
    }

    public void setId(Long id) {
        this.venueId = id;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
