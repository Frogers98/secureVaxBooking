package app.controller;

import app.exception.VenueNotFoundException;
import app.model.Appointment;
import app.model.User;
import app.model.Venue;
import app.repository.AppointmentRepository;
import app.repository.UserRepository;
import app.repository.VenueRepository;
import app.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller

@RequestMapping(path = "appointments")
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/list")
    public List listUsers() {
        return getAllAppointments();
    }

    public List<Appointment> getAllAppointments() { return appointmentRepository.findAll(); }

    @PostMapping
    public Appointment saveAppointment(Appointment appointment) {
        if (checkAptAlreadyExists(appointment.getVenue(),
                appointment.getDate(),
                appointment.getTime())) {
            System.out.println("An appointment has already been created at this time and date for " + appointment.getVenue() + ".");
            return null;
        }

        appointmentRepository.save(appointment);
        System.out.println("Appointment booked.");
        return appointment;
    }

    // a check to ensure repeat appointments at same time/day/venue
    public Boolean checkAptAlreadyExists(Venue venue, String date, String time) {
        var appointments = getAllAppointments();
        for (var apt: appointments) {
            if (Objects.equals(apt.getVenue(), venue) &&
                    Objects.equals(apt.getTime(), time) &&
                    Objects.equals(apt.getDate(), date)) return true;
        }
        return false;
    }

    @PostMapping("/bookAppointmentVenue")
    public String chooseVenue(@ModelAttribute("venue") Venue venueData,
                              Model model) {

        LocalDateTime now = LocalDateTime.now();
        String today = now.toString().split("T")[0];

        // list of appointments available today
        List<String> todayAppointments = todayAppointments();
        System.out.println("Appointments: " + todayAppointments);

        model.addAttribute("date", today);
        model.addAttribute("todayApts", todayAppointments);
        model.addAttribute("venue", venueData);
        model.addAttribute("appointment", new Appointment());
        return "book_appointment";
    }

    @PostMapping("/bookingSuccessful")
    public String bookingSuccessful(@RequestParam("date") String date,
                                    @RequestParam("time") String time,
                                    @RequestParam("vaccine") String vaccine,
                                    @RequestParam("id") Long venue_id,
                                    @AuthenticationPrincipal CustomUserDetails userDetails
                                    ) {

        String dose = "dose1";
        String userEmail = userDetails.getUsername();
        User user = userRepository.findByEmail(userEmail);
        Venue venue = venueRepository.getById(venue_id);

        Appointment newAppointment = new Appointment(
                vaccine,
                dose,
                date,
                time,
                venue);

        System.out.println("Appointment successfully created.");
        Appointment appointment = saveAppointment(newAppointment);
        System.out.println("appointment saved successfully");
        if (appointment == null) return "index";

        else {
            userRepository.updateUser(appointment.getApt_id(), user.getUser_id());
            return "booking_successful";
        }
    }

    // Get a Single Venue
    public Venue getVenueById(Long venue_id) throws VenueNotFoundException {
        return venueRepository.findById(venue_id)
                .orElseThrow(() -> new VenueNotFoundException(venue_id));
    }

    public List<String> todayAppointments() {
        // get today's date
        //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentHour = now.toString().split("T")[1].split(":")[0];

        List<String> hours = List.of("09:00", "10:00", "11:00",
                "12:00", "13:00", "14:00", "15:00", "16:00",
                "17:00", "18:00", "19:00", "20:00", "24:00");

        List<String> availableAppointments = new LinkedList<String>();
        for (String hour: hours) {
            if (Integer.parseInt(currentHour) < Integer.parseInt(hour.split(":")[0]))
                availableAppointments.add(hour);
        }

        return availableAppointments;
    }
}
