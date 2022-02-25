package app.repository;

import app.UserAptDetails;
import app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;


@Repository
public interface UserRepository
        extends JpaRepository<User, Long>{

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = ?1")
    User findByID(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users u set apt_id =:apt_id where u.id = :id",
            nativeQuery = true)
    void updateUser(@Param("apt_id") Long apt_id, @Param("id") Long id);

}
