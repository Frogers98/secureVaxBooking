package app.config;

import app.controller.UserController;
import app.model.Role;
import app.model.User;
import app.repository.RoleRepository;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleConfig {

    @Autowired
    RoleRepository roleRepo;

    @Bean
    CommandLineRunner commandLineRunnerAdmin(UserController controller) {
        return args -> {
            Role user = new Role("USER");
            Role admin = new Role("ADMIN");

            if (roleRepo.findByName("USER") == null){
                roleRepo.save(user);
            }

            if (roleRepo.findByName("ADMIN") == null){
                roleRepo.save(admin);
            }

            User Alex = new User(
                    "04-08-1991",
                    "Alex",
                    "Adminson",
                    "04123456",
                    "Sample Address",
                    "08745678",
                    "american",
                    "Other",
                    "alex@admin.com",
                    "password"
            );

            controller.registerAdmin(Alex);
        };
    }

}
