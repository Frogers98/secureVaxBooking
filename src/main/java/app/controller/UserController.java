package app.controller;

import app.UserAptDetails;
import app.repository.UserAptDetailsRepository;
import app.repository.UserRepository;
import app.model.User;
import app.exception.UserNotFoundException;

import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(path = "users")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserAptDetailsRepository userAptDetailsRepository;
    @Autowired
    AppointmentController appointmentController;

    public UserController() {
    }

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String showRegLoginLandingPage() {
        return "reg_login_landing";
    }

    @GetMapping("/register")
    public String startRegistration(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // register attempt of user with error checking for duplicate email or ppsn
    @PostMapping("/register_attempt")
    public String registerAttempt(@ModelAttribute("user") User newUser) {
        if (getUserByEmail(newUser.getEmail())) {
            System.out.println("An account associated with this email address has already been created.");
            return "reg_login_landing";
        }else if (getUserByPPSN(newUser.getPpsn())) {
            System.out.println("An account associated with this PPS number has already been created.");
            return "reg_login_landing";
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(newUser.getPassword());
            newUser.setPassword(encodedPassword);
//            userRepository.save(newUser);
            userService.registerDefaultUser(newUser);
            System.out.println("User saved");
            return "success_reg";
        }
    }

    public void registerAdmin(User newUser) {
        if (getUserByEmail(newUser.getEmail())) {
            System.out.println("An account associated with this email address has already been created.");
        }else if (getUserByPPSN(newUser.getPpsn())) {
            System.out.println("An account associated with this PPS number has already been created.");
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(newUser.getPassword());
            newUser.setPassword(encodedPassword);
//            userRepository.save(newUser);
            userService.registerAdminUser(newUser);
            System.out.println("Admin User saved");
        }
    }

    // Test connection to frontend
    @RequestMapping("/welcome")
    public String welcome(){
        return "Welcome!";
    }

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

        var user =  users.stream()
                .filter(t -> ppsn.equals(t.getPpsn()))
                .findFirst()
                .orElse(null);

        if (user == null) return false;
        else return true;
    }

    // Get a Single User
    @GetMapping("/{id}")
    public User getUserById(@PathVariable(value = "id") Long userId)
            throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    // should this not be getMapping? I'm not sure
    // register a user id with an appointment
    @GetMapping("/apt/{id}/{apt_id}")
    public String bookAppointment(@PathVariable (value = "id") Long userId,
                                  @PathVariable (value = "apt_id") Long apt_id) throws UserNotFoundException {
        User queryUser = userRepository.findByID(userId);
        System.out.println("Altering user: " + queryUser.getName());
        userRepository.updateUser(apt_id, userId);
        showAppointment(3L);
        return "appointment_booked";
    }

    // return appointment details of a user - incomplete pending team decisions on functionality
    public void showAppointment(Long userId) throws UserNotFoundException {
        UserAptDetails userAptDetails = userAptDetailsRepository.findAptDetails(userId);
        System.out.println(userAptDetails.getApt_id() + " " + userAptDetails.getVenue());
    }

    @GetMapping("/edit")
    public String editUsers() {
        return "edit_users";
    }
}