package app.repository;

import app.UserAptDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAptDetailsRepository
        extends JpaRepository<UserAptDetails, Long> {

    @Query(value = "SELECT u.name, " +
            "u.surname, u.user_id, u.apt_id, a.venue_id " +
            "FROM users u INNER JOIN appointments a ON u.apt_id = a.apt_id where u.user_id = :user_id",
            nativeQuery = true)
    UserAptDetails findAptDetails(@Param("user_id") Long user_id);
}
