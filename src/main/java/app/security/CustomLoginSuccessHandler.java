package app.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.model.User;
import app.repository.UserRepository;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

// Adapted from https://www.codejava.net/frameworks/spring-boot/spring-security-limit-login-attempts-example
@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JavaMailSender mailSender;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userDetails =  (CustomUserDetails) authentication.getPrincipal();
        System.out.println("entered login success handler");

        User user = userDetails.getUser();

        if (!user.isOTPRequired()){
            try {
                // Generate and send One Time Passcode, effectively setting OTP to true
                generateOTP(user);
                throw new InsufficientAuthenticationException("OTP");
            } catch (MessagingException | UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new AuthenticationServiceException("OTP could not be sent");
            }
        } else {
            user.setOneTimePassword(null);
            user.setOtpRequestedTime(null);
            userRepository.save(user);
        }

        System.out.println("login succesful");
        if (user.getFailedAttempt() > 0) {
            userService.resetFailedAttempts(user.getEmail());
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

    /* Generates a random one time passcode, sends to database for later matching */
    private void generateOTP(User user) throws MessagingException, UnsupportedEncodingException {
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        String OTP = Integer.toString(n);
        System.out.println("Generated OTP = " + OTP);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedOTP = passwordEncoder.encode(OTP);
        user.setOneTimePassword(encodedOTP);
        System.out.println("Encoded OTP = " + encodedOTP);
        user.setOtpRequestedTime(new Date());
        userRepository.save(user);
        sendOTP(user, OTP);
    }

    /* Sends out email to user's email address containing One Time Passcode */
    private void sendOTP(User user, String OTP) throws MessagingException, UnsupportedEncodingException {
        System.out.println("Sending");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("securevaxbooking@gmail.com", "Vax Booking");
        helper.setTo(user.getEmail());
        String subject = "One Time Password for Vax Booking";
        String content = "<p>Hello " + user.getName() + ", please use this one time passcode to complete login:  <br> <br> <b>" +
                OTP + "<b><br><br> This code will expire in 5 minutes </b></b></p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        System.out.println("Still sending");
        mailSender.send(message);
        System.out.println("Sent");
    }

}
