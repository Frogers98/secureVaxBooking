package app.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import app.model.User;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

// Class added while following this guide: https://www.codejava.net/frameworks/spring-boot/spring-security-limit-login-attempts-example
@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserService userService;

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
                    System.out.println("login attempt failed, less than 3 failed attempts");
                    userService.increaseFailedAttempts(user);
                } else {
                    System.out.println("login attempt failed, more than 3 failed attempts");
                    userService.increaseFailedAttempts(user);
                    userService.lock(user);
                    exception = new LockedException("Your account has been locked due to 3 failed attempts."
                            + " It will be unlocked after 24 hours.");
                }
            } else if (!user.isAccountNonLocked()) {
                System.out.println("Account is locked");
                if (userService.unlockWhenTimeExpired(user)) {
                    exception = new LockedException("Your account has been unlocked. Please try to login again.");
                }
            }

        }


        System.out.println("Failure Handler, email: " + email);
        failureRedirectURL = "/login?error&email=" + email;
        if (exception.getMessage().contains("OTP")) {
            failureRedirectURL = "/login?otp=true&email=" + email;
        } else {
            if (user != null && user.isOTPRequired()) {
                failureRedirectURL = "/login?otp=true&email=" + email;
            }
        }

        super.setDefaultFailureUrl(failureRedirectURL);
        super.onAuthenticationFailure(request, response, exception);
    }

}
