package app.controller;

import app.repository.UserRepository;
import app.model.User;
import app.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    // Get All users
    @GetMapping
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
}