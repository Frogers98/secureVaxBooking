package app.config;

import app.controller.AppointmentController;
import app.model.Appointment;
import app.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppointmentConfig {

    @Bean
    CommandLineRunner aptRunner(AppointmentController aptController) {
        return args -> {
            aptController.newAppointment(
                    "pfizer",
                    "Dose1",
                    "03-01-2021",
                    "16:00",
                    "Dublin");
        };
    }
}
