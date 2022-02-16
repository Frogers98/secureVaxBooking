package app.controller;

import app.repository.UserRepository;
import app.model.User;
import app.exception.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(path = "users")
public class UserController {

    @Autowired
    UserRepository userRepository;

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
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
        userRepository.save(newUser);
        System.out.println("User saved");
        return "registered_successfully";
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

    // Get a Single User
    @GetMapping("/{id}")
    public User getUserById(@PathVariable(value = "id") Long userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
