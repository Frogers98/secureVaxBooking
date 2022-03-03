package app.config;

import app.controller.VenueController;
import app.model.Venue;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VenueConfig {

    @Bean
    CommandLineRunner venueRunner(VenueController controller) {
        return args -> {
            Venue Dublin = new Venue(
                    1L,
                    "Dublin",
                    "UCD"
            );

            Venue Mayo = new Venue(
                    2L,
                    "Mayo",
                    "Westport"
            );

            controller.newVenue(Mayo);
            controller.newVenue(Dublin);
        };
    }
}
