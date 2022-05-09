package app.repository;

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

    @Query("SELECT u FROM User u WHERE u.user_id = ?1")
    User findByID(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users u set apt_id =:apt_id where u.user_id = :user_id",
            nativeQuery = true)
    void updateUserAppointment(@Param("apt_id") Long apt_id, @Param("user_id") Long user_id);


    @Transactional
    @Modifying
    @Query(value = "UPDATE users u set u.apt_id = null where u.user_id = :user_id",
            nativeQuery = true)
    void cancelUserAppointment(@Param("user_id") Long user_id);

    @Transactional
    @Modifying
    @Query("UPDATE User u set u.dose1 = ?1 where u.user_id = ?2")
    void updateDose1(String dose1, Long user_id);

    @Transactional
    @Modifying
    @Query("UPDATE User u set u.dose1Date = ?1 where u.user_id = ?2")
    void updateDose1Date(String dose1Date, Long user_id);

    @Transactional
    @Modifying
    @Query("UPDATE User u set u.dose2 = ?1 where u.user_id = ?2")
    void updateDose2(String dose2, Long user_id);

    @Transactional
    @Modifying
    @Query("UPDATE User u set u.dose2Date = ?1 where u.user_id = ?2")
    void updateDose2Date(String dose2Date, Long user_id);

    // adapted from https://www.codejava.net/frameworks/spring-boot/spring-security-limit-login-attempts-example
    @Query("UPDATE User u SET u.failedAttempt = ?1 WHERE u.email = ?2")
    @Modifying
    public void updateFailedAttempts(int failAttempts, String email);
}
