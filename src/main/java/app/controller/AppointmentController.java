package app.controller;

import app.exception.UserNotFoundException;
import app.model.Appointment;
import app.model.User;
import app.repository.AppointmentRepository;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller

@RequestMapping(path = "appointments")
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @GetMapping("/list")
    public List listUsers() {
        return getAllAppointments();
    }

    public List<Appointment> getAllAppointments() { return appointmentRepository.findAll(); }

    @PostMapping
    public String saveAppointment(Appointment appointment) {
        if (getAptById(appointment.getVenue(),
                appointment.getDate(),
                appointment.getTime())) {
            System.out.println("An appointment has already been created at this time and date for " + appointment.getVenue() + ".");
            // should probably replace this with an error page of some form
            return "index";
        }
        appointmentRepository.save(appointment);
        System.out.println("Appointment booked.");
        return "appointment_booked";
    }

    // a check to ensure repeat appointments at same time/day/venue
    public Boolean getAptById(String venue, String time, String date) {
        var appointments = getAllAppointments();
        var appointmentCheck = appointments.stream()
                .filter(v -> venue.equals(v.getVenue()))
                .filter(v -> time.equals(v.getTime()))
                .filter(v -> date.equals(v.getDate()));

        if (appointmentCheck == null) return false;
        else return true;
    }
}