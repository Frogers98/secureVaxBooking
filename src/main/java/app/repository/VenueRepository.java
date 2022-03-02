package app.repository;

import app.model.User;
import app.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepository
        extends JpaRepository<Venue, Long> {

    @Query("SELECT v FROM Venue v WHERE v.venue_name = ?1")
    Venue findByVenueName(String venue_name);

    @Query("SELECT v FROM Venue v WHERE v.venue_id = ?1")
    Venue findByID(Long id);
}
