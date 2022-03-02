package app.service;

import app.exception.UserNotFoundException;
import app.model.User;
import app.repository.UserRepository;
import app.repository.forum.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForumService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    PostRepository postRepo;


//    public void registerDefaultUser(User user) {
//        Role roleUser = roleRepo.findByName("User");
//        user.addRole(roleUser);
//        userRepo.save(user);
//    }

    public User getUserById(Long id) throws UserNotFoundException {
//        return userRepo.findById(id);
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
    }

