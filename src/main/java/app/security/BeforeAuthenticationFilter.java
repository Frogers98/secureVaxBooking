package app.security;

import app.model.User;
import app.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class BeforeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

//    @Autowired
//    private CustomerServices customerService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authManager) {
        super.setAuthenticationManager(authManager);
    }

    @Autowired
    @Override
    public void setAuthenticationFailureHandler(
            AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(failureHandler);
    }

    @Autowired
    @Override
    public void setAuthenticationSuccessHandler(
            AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(successHandler);
    }

    public BeforeAuthenticationFilter() {
//        setUsernameParameter("email");
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
       String email = request.getParameter("username");
        System.out.println("attemptAuthentication, email: " + email);
        User user = userRepository.findByEmail(email);
        // Check if user exists
        if (user != null) {
            if (user.isOTPRequired()){
                return super.attemptAuthentication(request, response);
            }
            try {
                generateOTP(user);
                throw new InsufficientAuthenticationException("OTP");
            } catch (MessagingException | UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new AuthenticationServiceException("OTP could not be sent");
            }

        }
       return super.attemptAuthentication(request, response);
    }

    private float getGoogleRecaptchaScore() {
        return 1;
    }

    /* Generates a random one time passcode, sends to database for later matching */
    private void generateOTP(User user) throws MessagingException, UnsupportedEncodingException {
        String OTP = RandomString.make(8);
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
        String content = "<p>Hello " + user.getName() + ", please use this one time passcode to complete login:  <br> <b>" +
                OTP + "<b><br> This code will expire in 5 minutes </p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        System.out.println("Still sending");
        mailSender.send(message);
        System.out.println("Sent");
    }

}