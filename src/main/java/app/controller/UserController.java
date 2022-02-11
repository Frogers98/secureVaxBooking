//package app.controller;
//
//import app.model.User;
//import app.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import app.repository.UserRepository;
//import javax.validation.Valid;
//import java.util.List;
//
//@RestController
//@RequestMapping(path = "api/users")
//public class UserController {
//
//    private final UserService userService;
//
//    @Autowired
//    public UserController(UserService userService) {
//
//        this.userService = userService;
//    }
//
//    @GetMapping
//    public List<User> getUsers() {
//        return userService.getUsers();
//    }
//
//    @PostMapping
//    public User newUser(@Valid @RequestBody User newUser) {
//        return userService.newUser(newUser);
//    }
//}