package app.controller;

import app.exception.VenueNotFoundException;
import app.model.Appointment;
import app.model.User;
import app.model.Venue;
import app.repository.AppointmentRepository;
import app.repository.UserRepository;
import app.repository.VenueRepository;
import app.security.CustomUserDetails;
import app.test;
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
    public String chooseVenue(@RequestParam("venue_id") String venueData,
                              @RequestParam("date") String date,
                              Model model) throws VenueNotFoundException {

        LocalDateTime now = LocalDateTime.now();
        String today = now.toString().split("T")[0];

        // venue matching returned String
        Venue venue = getVenueById(Long.parseLong(venueData));

        // get list of available appointments
        List<String> availableAppointmentTimes = checkAvailableAppointments(venue, date, today);

        if (availableAppointmentTimes.size() > 0) {
            model.addAttribute("test", new test(date, venueData));
            model.addAttribute("todayApts", availableAppointmentTimes);
            model.addAttribute("appointment", new Appointment());
            return "book_appointment";
        }

        else return "no_appointments_available";
    }

    @PostMapping("/bookingSuccessful")
    public String bookingSuccessful(@RequestParam("date") String date,
                                    @RequestParam("time") String time,
                                    @RequestParam("vaccine") String vaccine,
                                    @RequestParam("venue_id") Long venue_id,
                                    @AuthenticationPrincipal CustomUserDetails userDetails
                                    ) {

        String userEmail = userDetails.getUsername();
        User user = userRepository.findByEmail(userEmail);
        Venue venue = venueRepository.getById(venue_id);

        // we need to know the users current dose status
        String dose = "dose1";
        if (user.getDose1Date() != null) dose = "dose2";
        if (user.getDose2Date() != null) return "dose3";

        Appointment newAppointment = new Appointment(
                vaccine,
                dose,
                date,
                time,
                venue);

        Appointment appointment = saveAppointment(newAppointment);
        System.out.println("Appointment saved successfully");
        if (appointment == null) return "index";

        else {
            userRepository.updateUserAppointment(appointment.getApt_id(), user.getUser_id());
            return "booking_successful";
        }
    }

    private List<String> checkAvailableAppointments(Venue venue, String date, String today) {
        List<String> availableAppointmentTimes = appointments();
        if (today.equals(date)) {
            // list of appointments available today
            availableAppointmentTimes = todaysAppointments();
        }

        List<String> availableAppointments = new LinkedList<>();
        for (String appointment_time: availableAppointmentTimes) {
            if (!checkAptAlreadyExists(venue, date, appointment_time))
                availableAppointments.add(appointment_time);
        }
        return availableAppointments;
    }

    public List<String> todaysAppointments() {
        // get today's date
        LocalDateTime now = LocalDateTime.now();
        String currentHour = now.toString().split("T")[1].split(":")[0];
        List<String> hours = appointments();

        List<String> availableAppointments = new LinkedList<>();
        for (String hour: hours) {
            if (Integer.parseInt(currentHour) < Integer.parseInt(hour.split(":")[0]))
                availableAppointments.add(hour);
        }
        return availableAppointments;
    }

    public List<String> appointments() {
        return List.of("09:00", "10:00", "11:00",
                "12:00", "13:00", "14:00", "15:00", "16:00",
                "17:00", "18:00", "19:00", "20:00", "24:00");
    }

    // Get a Single Venue
    public Venue getVenueById(Long venue_id) throws VenueNotFoundException {
        return venueRepository.findById(venue_id)
                .orElseThrow(() -> new VenueNotFoundException(venue_id));
    }

}
