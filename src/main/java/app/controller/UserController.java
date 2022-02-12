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

        return userRepository.findAll();
    }

    @PostMapping
    public User newUser(@Valid @RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    // Get a Single User
    @GetMapping("/{id}")
    public User getUserById(@PathVariable(value = "id") Long userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
