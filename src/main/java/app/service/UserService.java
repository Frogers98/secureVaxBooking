package app.service;

import java.util.List;

import app.model.User;
import app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public String saveEmployee(User user)
    {
        repository.save(user);
        return "saved Employee Resource";
    }

    public List<User> getAllUsers()
    {
        return repository.findAll();
    }

}