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
//            Venue venueWithId = venueRepository.findByVenueName("Mayo");
//
//            Appointment appointment = new Appointment(
//                    "pfizer",
//                    "Dose1",
//                    "03-01-2021",
//                    "16:00",
//                    venueWithId);
//
//            System.out.println("test");
//            System.out.println(appointment);
//            aptController.saveAppointment(appointment);
//            System.out.println("other test");
//        };
//
//    }
//}
