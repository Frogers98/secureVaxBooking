//package app.config;
//
//import app.controller.AppointmentController;
//import app.controller.VenueController;
//import app.model.Appointment;
//import app.model.User;
//import app.model.Venue;
//import app.repository.VenueRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AppointmentConfig {
//
//    @Autowired
//    VenueRepository venueRepository;
//
//    @Bean
//    CommandLineRunner aptRunner(AppointmentController aptController, VenueController venueController) {
//        return args -> {
//            Venue mayo = new Venue(
//                    "Mayo",
//                    "Westport"
//            );
//
//            venueController.newVenue(mayo);
//            Thread.sleep(5000);
//
//            Venue venueWithId = venueRepository.findByVenueName("Mayo");
//            Thread.sleep(2000);
//
//            Appointment appointment = new Appointment(
//                    "pfizer",
//                    "Dose1",
//                    "03-01-2021",
//                    "16:00",
//                    venueWithId);
//
//
//
//            aptController.saveAppointment(appointment);
//        };
//
//    }
//}