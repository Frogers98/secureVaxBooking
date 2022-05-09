package app.security;

import app.model.User;
import app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        String email = request.getParameter("username");
        System.out.println("Failure Handler, email: " + email);
        String failureRedirectURL = "/login?error&email=" + email;
        if (exception.getMessage().contains("OTP")){
            failureRedirectURL = "/login?otp=true&email=" + email;
        } else {
            User user = userRepository.findByEmail(email);
            if (user.isOTPRequired() && user != null){
                failureRedirectURL = "/login?otp=true&email=" + email;
            }
        }

        super.setDefaultFailureUrl(failureRedirectURL);
        super.onAuthenticationFailure(request, response, exception);
    }

}
