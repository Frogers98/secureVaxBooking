package app.controller;


import app.UserAptDetails;
import app.repository.UserAptDetailsRepository;
import app.repository.UserRepository;
import app.model.User;
import app.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
            // should probably replace this with an error page of some form
            return "index";
        }else if (getUserByPPSN(newUser.getPpsn())) {
            System.out.println("An account associated with this PPS number has already been created.");
            // should probably replace this with an error page of some form
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

    // should this not be getMapping? I'm not sure
    @GetMapping("/apt/{id}/{apt_id}")
    public String bookAppointment(@PathVariable (value = "id") Long userId,
                                  @PathVariable (value = "apt_id") Long apt_id) throws UserNotFoundException {
        User queryUser = userRepository.findByID(userId);
        System.out.println(queryUser.getName());
        userRepository.updateUser(apt_id, userId);
        showAppointment(3L);
        return "appointment_booked";
    }

    // return appointment details of a user - incomplete pending team decisions on functionality
    public void showAppointment(Long userId) throws UserNotFoundException {
        UserAptDetails userAptDetails = userAptDetailsRepository.findAptDetails(userId);
        System.out.println(userAptDetails.getApt_id() + " " + userAptDetails.getVenue());
    }
}