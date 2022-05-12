package app.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import app.controller.DefaultController;
import app.model.IncorrectLogin;
import app.model.User;
import app.repository.IncorrectLoginRepo;
import app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

// Class added while following this guide: https://www.codejava.net/frameworks/spring-boot/spring-security-limit-login-attempts-example
@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000; // 1 day

    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private IncorrectLoginRepo incorrectLoginRepo;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String failureRedirectURL = "/login?error";
        String email = request.getParameter("username");
        System.out.println("email used is " + email);

        User user = userService.getByEmail(email);


        System.out.println("entered login failure handler");
        if (user != null) {
            System.out.println(user.getName());
            System.out.println("user is not null");

            // Check the ipAddress used for this request and lock it if need be
            String ipAddress = request.getRemoteAddr();
            System.out.println("ip is: " + ipAddress);

            // Check if this login has tried to login unsuccessfuly twice previously and lock it if so
            // otherwise just increment the number of attempts
            // This should maybe be wrapped in a try catch in case the db call fails and this code should
            // maybe be moved to another part of this class so it can still run even if the account being
            // accessed is already blocked
            IncorrectLogin incorrectLogin = incorrectLoginRepo.findByip(ipAddress);

            if (incorrectLogin == null) {
                // If this ip address hasn't attempted to login before then create a new entry in the db
                incorrectLogin = new IncorrectLogin(ipAddress);
                incorrectLoginRepo.save(incorrectLogin);
                logger.warn("New IP address logged as failing login attempt: " + ipAddress);
            } else {

                int numAttemptsIP = incorrectLogin.getNumAttempts();

                if (incorrectLogin.isIpNonLocked()) {

                    if (numAttemptsIP < 2) {
                        System.out.println("login attempt failed, less than 3 failed attempts from this ip");
                        logger.warn("IP address logged as failing login attempt again: " + ipAddress);
                        incorrectLoginRepo.updateFailedAttempts(numAttemptsIP + 1, ipAddress);
                    }
                    if (numAttemptsIP >= 2) {
                        incorrectLoginRepo.updateFailedAttempts(numAttemptsIP + 1, ipAddress);
                        incorrectLogin.setIpNonLocked(false);
                        incorrectLogin.setLockTime(new Date());
                        incorrectLoginRepo.save(incorrectLogin);
                        logger.warn("IP address locked after failing login 3 times: " + ipAddress);
                        exception = new LockedException("Your ip address has been locked due to 3 failed attempts from this ip");
                    }
                } else {
                    long lockTimeInMillis = incorrectLogin.getLockTime().getTime();
                    long currentTimeInMillis = System.currentTimeMillis();

                    if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
                        incorrectLogin.setIpNonLocked(true);
                        incorrectLogin.setLockTime(null);
                        incorrectLogin.setNumAttempts(0);

                        incorrectLoginRepo.delete(incorrectLogin);
                        exception = new LockedException("Your ip address has been unlocked. Please try to login again.");
                        logger.info("IP address no longer locked after sign-in attempt - lock-out duration elapsed: " + ipAddress);
                    }
                }

            }

                if (user.isAccountNonLocked()) {

                    // Euegenes stuff
                    System.out.println("Failure Handler, email: " + email);
                    failureRedirectURL = "/login?error&email=" + email;
                    if (exception.getMessage().contains("OTP")) {
                        failureRedirectURL = "/login?otp=true&email=" + email;
                    } else {
                        if (user != null && user.isOTPRequired()) {
                            failureRedirectURL = "/login?otp=true&email=" + email;
                        }
                    }

                    // Check number of failed attempts of account logging in
                    if (user.getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS - 1) {
                        System.out.println("login attempt failed, less than 3 failed attempts: " + user.getFailedAttempt());
                        userService.increaseFailedAttempts(user);
                        logger.warn("Account logged as failing login attempt: " + email);
                    } else {
                        System.out.println("login attempt failed, more than 3 failed attempts: " + user.getFailedAttempt());
                        userService.increaseFailedAttempts(user);
                        userService.lock(user);
                        logger.warn("Account locked after failing 3 login attempts: " + email);
                        exception = new LockedException("Your account has been locked due to 3 failed attempts."
                                + " It will be unlocked after 24 hours.");
                    }
                } else if (!user.isAccountNonLocked()) {
                    System.out.println("Account is locked");
                    if (userService.unlockWhenTimeExpired(user)) {
                        logger.info("Account no longer locked after sign-in attempt - lock-out duration elapsed: " + email);
                        exception = new LockedException("Your account has been unlocked. Please try to login again.");
                    }
                }

            }


            super.setDefaultFailureUrl(failureRedirectURL);
            super.onAuthenticationFailure(request, response, exception);
        }

    }

