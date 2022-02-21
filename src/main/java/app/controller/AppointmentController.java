package app.controller;

import app.model.Appointment;
import app.model.User;
import app.repository.AppointmentRepository;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

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
    public void newAppointment(@Valid @RequestBody
                               String vaccine,
                               String dose,
                               String date,
                               String time,
                               String venue) {

        Appointment apt = new Appointment(vaccine,
                                            dose,
                                            date,
                                            time,
                                            venue);

        appointmentRepository.save(apt);
    }

    public Appointment getAptById(Long apt_id) {
        var apts = getAllAppointments();

        var user =  apts.stream()
                .filter(t -> apt_id.equals(t.getApt_id()))
                .findFirst()
                .orElse(null);

        return user;
    }
}
