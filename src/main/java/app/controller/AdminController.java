package app.controller;

import app.repository.UserRepository;
import app.model.User;
import app.exception.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "admin")
public class AdminController {

    @Autowired
    UserRepository userRepository;

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

    @RequestMapping("/logout")
    public String logout(){
        // Login status= false
        // Redirect to home page
        return "Welcome!";
    }

    // Get All users
    /* Convert this to Admin only w frontend view OR delete before submission */
    @GetMapping("/showAll")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @PostMapping("/createNew")
    public User newUser(@Valid @RequestBody User newUser)  {
        // Error check if user already exists - column unique now in model

        return userRepository.save(newUser);
    }

    // Get a Single User
    @GetMapping("/{id}")
    public User getUserById(@PathVariable(value = "id") Long userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}

