package app.config;

import app.model.User;
import app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            User Mariam = new User(
                    "03-07-1990",
                    "Mariam",
                    "Jamal",
                    "040404040",
                    "Sample Address",
                    "08711111",
                    "mariam.jamal@gmail.com",
                    "password"
            );

            userRepository.saveAll(
                    List.of(Mariam)
            );
        };
    }
}