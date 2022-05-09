package app.security;

import app.model.User;
import app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BeforeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

//    @Autowired
//    private CustomerServices customerService;

    @Autowired
    UserRepository userRepository;

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
        if (user != null) {
            // He gets google captcha score here
            throw new InsufficientAuthenticationException("OTP");
        }
       return super.attemptAuthentication(request, response);
    }

    private float getGoogleRecaptchaScore() {
        return 1;
    }

}