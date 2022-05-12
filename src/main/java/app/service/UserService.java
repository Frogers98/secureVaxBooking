package app.service;

import app.model.IncorrectLogin;
import app.model.Role;
import app.model.User;
import app.repository.IncorrectLoginRepo;
import app.repository.RoleRepository;
import app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    // guide from https://www.codejava.net/frameworks/spring-boot/spring-security-limit-login-attempts-example
    // followed for some sections for locking account after too many attempts

    public static final int MAX_FAILED_ATTEMPTS = 3;
    // lock of one day
    private static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private IncorrectLoginRepo incorrectLoginRepo;

    @Autowired
    RoleRepository roleRepo;

    public void registerDefaultUser(User user) {
        Role roleUser = roleRepo.findByName("User");
        user.addRole(roleUser);
        userRepo.save(user);
    }

    public void registerAdminUser(User user) {
        Role roleUser = roleRepo.findByName("ADMIN");
        user.addRole(roleUser);
        userRepo.save(user);
    }

    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        userRepo.updateFailedAttempts(newFailAttempts, user.getEmail());
    }

    public void resetFailedAttempts(String email) {
        userRepo.updateFailedAttempts(0, email);
    }

    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());

        userRepo.save(user);
    }

    public boolean unlockWhenTimeExpired(User user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);

            userRepo.save(user);

            return true;
        }

        return false;
    }

//    public boolean unlockWhenTimeExpired(IncorrectLogin incorrectLogin) {
//        long lockTimeInMillis = incorrectLogin.getLockTime().getTime();
//        long currentTimeInMillis = System.currentTimeMillis();
//
//        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
//            incorrectLogin.setIpNonLocked(true);
//            incorrectLogin.setLockTime(null);
//            incorrectLogin.setNumAttempts(0);
//
//            incorrectLoginRepo.save(incorrectLogin);
//
//            return true;
//        }
//
//        return false;
//    }

    public User getByEmail(String email) {
        User user = userRepo.findByEmail(email);
        return user;
    }


}