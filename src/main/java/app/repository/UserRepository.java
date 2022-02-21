package app.repository;

import app.UserAptDetails;
import app.model.Appointment;
import app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository
        extends JpaRepository<User, Long>{

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = ?1")
    public User findByID(Long id);

    @Query(value = "SELECT u (u.name, u.surname, u.id, u.apt_id, a.venue) FROM users u INNER JOIN appointments a ON u.apt_id = a.apt_id where u.id = :id",
            nativeQuery = true)
    List<UserAptDetails> findAptDetails(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users u set apt_id =:apt_id where u.id = :id",
            nativeQuery = true)
    void updateUser(@Param("apt_id") int apt_id, @Param("id") Long id);

}
