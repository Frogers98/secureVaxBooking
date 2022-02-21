package app.controller;


import app.UserAptDetails;
import app.model.Appointment;
import app.repository.UserRepository;
import app.model.User;
import app.exception.UserNotFoundException;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.naming.Name;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.sql.Types;
import java.util.List;

@Controller
@RequestMapping(path = "users")
public class UserController {

    @Autowired
    private DataSource dataSource;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AppointmentController appointmentController;

    public UserController() {
    }

    @GetMapping("")
    public String showIndexPage() {
        return "index";
    }

    @GetMapping("/register")
    public String startRegistration(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register_attempt")
    public String registerAttempt(@ModelAttribute("user") User newUser) {
        if (getUserByEmail(newUser.getEmail())) {
            System.out.println("An account associated with this email address has already been created.");
            return "index";
        }else if (getUserByPPSN(newUser.getPpsn())) {
            System.out.println("An account associated with this PPS number has already been created.");
            return "index";
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(newUser.getPassword());
            newUser.setPassword(encodedPassword);
            userRepository.save(newUser);
            System.out.println("User saved");
            return "registered_successfully";
        }
    }

    // Test connection to frontend
    @RequestMapping("/welcome")
    public String welcome(){
        return "Welcome!";
    }
  /*https://www.codejava.net/frameworks/spring-boot/user-registration-and-login-tutorial*/
    @RequestMapping("/login")
    public String login(){
        // Take username and password
        // Check against database
        // Login status= true
        // Redirect to my details page
        return "Welcome!";
    }

    @GetMapping("/listUsers")
    public String listUsers(Model model) {
        List<User> allUsers = userRepository.findAll();
        model.addAttribute("listUsers", allUsers);
        return "list_users";
    }

    @RequestMapping("/logout")
    public String logout(){
        // Login status= false
        // Redirect to home page
        return "Welcome!";
    }

    // Get All users
    public List<User> getAllUsers(){
        return  userRepository.findAll();
    }

    // Get a Single User
    // currently redundant
    @GetMapping("/{id}")
    public User getUserById(@PathVariable(value = "id") Long userId)
            throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    // altered function to only save user if email not already taken
    @PostMapping
    public void newUser(@Valid @RequestBody User newUser) {
        if (getUserByEmail(newUser.getEmail()))
            System.out.println("An account associated with this email address has already been created.");
        else if (getUserByPPSN(newUser.getPpsn()))
            System.out.println("An account associated with this PPS number has already been created.");
        else
            userRepository.save(newUser);
    }

    // I will try to consolidate this and the email check into one method
    public Boolean getUserByEmail(String email) {
        var users = getAllUsers();

        var user =  users.stream()
                .filter(t -> email.equals(t.getEmail()))
                .findFirst()
                .orElse(null);

        if (user == null) return false;
        else return true;
    }

    public Boolean getUserByPPSN(String ppsn) {
        var users = getAllUsers();

        var user = users.stream()
                .filter(t -> ppsn.equals(t.getPpsn()))
                .findFirst()
                .orElse(null);

        if (user == null) return false;
        else return true;
    }

    public void bookAppointment() throws UserNotFoundException {
//        userRepository.updateUser(12, 3L);
        User queryUser = userRepository.findByID(3L);
        System.out.println(queryUser.getName());
        showAppointment(3L);
    }

    public void showAppointment(Long userId) throws UserNotFoundException {
        List<UserAptDetails> queryUser = userRepository.findAptDetails(userId);
        System.out.println(queryUser);
    }
}