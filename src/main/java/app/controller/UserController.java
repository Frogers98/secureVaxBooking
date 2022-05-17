package app.controller;

import app.dataValidation.PasswordConstraintValidator;
import app.exception.bookAppointmentException;
import app.model.Appointment;
import app.model.Venue;
import app.repository.AppointmentRepository;
import app.repository.UserAptDetailsRepository;
import app.repository.UserRepository;
import app.model.User;

import app.security.CustomUserDetails;
import app.service.UserService;
import app.VenueAndDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static app.dataValidation.InputValidation.*;


@Controller
@RequestMapping(path = "users")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserAptDetailsRepository userAptDetailsRepository;
    @Autowired
    AppointmentController appointmentController;
    @Autowired
    AppointmentRepository appointmentRepository;


    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

    public UserController() {
    }

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String showRegLoginLandingPage() {
        return "reg_login_landing";
    }

    @GetMapping("/register")
    public String startRegistration(Model model) {
        String errorMessage = null;
        model.addAttribute("user", new User());
        model.addAttribute("errorMessage", errorMessage);
        return "register";
    }

    // register attempt of user with error checking for duplicate email or ppsn
    @PostMapping("/register_attempt")
    public String registerAttempt(@ModelAttribute("user") User newUser, Model model) throws IOException {
        String pwdRequirements = PasswordConstraintValidator.CheckValid(newUser.getPassword());

        // email format verification
        String userEmail = newUser.getEmail();
        if (!validateEmailFormat(userEmail)) {
            errorMessage("Invalid email", model);
            return "register";
        }

        // unique email verification
        if (getUserByEmail(newUser.getEmail())) {
            errorMessage("An account associated with this email address has already been created.", model);
            return "register";
        }

        // pps format validation
        if (!ppsnValid(newUser.getPpsn())) {
            errorMessage("Invalid PPSN.", model);
            return "register";
        }

        // unique pps verification
        if (getUserByPPSN(newUser.getPpsn())) {
            errorMessage("An account associated with this PPS number has already been created.", model);
            logger.trace("tracing the same PPS number");
            return "register";
        }

        // password format validation
        if (!pwdRequirements.equals("1")) {
            model.addAttribute("errorMessage", pwdRequirements);
            return "register";
        }

        // validate phone number format
        if (!validatePhoneNumberFormat(newUser.getPhone())) {
            errorMessage("Phone Number Invalid - please ensure correct Irish phone number.", model);
            return "register";
        }

        if (!ValidateAddressFormat(newUser.getAddress())) {
            errorMessage("Address exceeds 100 characters, or contains special characters.", model);
            return "register";
        }

        if (!ValidateForenameInput(newUser.getAddress())) {
            errorMessage("Forename exceeds 100 characters, or contains special characters.", model);
            return "register";
        }

        if (!ValidateSurnameInput(newUser.getAddress())) {
            errorMessage("Surname exceeds 100 characters, or contains special characters.", model);
            return "register";
        }


        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
        userService.registerDefaultUser(newUser);
        logger.info("New account has been registered.");
        System.out.println("User saved");
        return "success_reg";
    }

    public void registerAdmin(User newUser) {
        if (getUserByEmail(newUser.getEmail())) {
            System.out.println("Admin: An account associated with this email address has already been created.");
            System.out.println(newUser.getDob());
        } else if (getUserByPPSN(newUser.getPpsn())) {
            System.out.println("Admin: An account associated with this PPS number has already been created.");
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(newUser.getPassword());
            newUser.setPassword(encodedPassword);
            userService.registerAdminUser(newUser);
            System.out.println("Admin User saved");
            logger.info("New Admin account has been registered.");
        }
    }


    @GetMapping("/listUsers")
    public String listUsers(Model model) {
        List<User> allUsers = userRepository.findAll();
        model.addAttribute("listUsers", allUsers);
        logger.warn("List of Users has been Accessed.");
        return "list_users";
    }


    @GetMapping("/myInfo")
    public String showMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        String userEmail = userDetails.getUsername();
        User user = userRepository.findByEmail(userEmail);
        model.addAttribute("user", user);
        Appointment nextApt = checkAptExists(user);
        model.addAttribute("apt", nextApt);
        logger.warn("My Info page accessed of ID: "+ user.getUser_id());

        return "my_info";
    }


    public Appointment checkAptExists(User user) {
        Appointment nextApt = user.getNextApptId();
        if (nextApt == null) System.out.println("Next Apt: "  + nextApt);
        else System.out.println("Next Apt: "  + nextApt.toString());
        return nextApt;
    }


    @GetMapping("/editUserInfo/{id}")
    public String editUserInfo(@PathVariable(value = "id") Long userId, Model model) {
        User user = userRepository.findByID(userId);
        model.addAttribute("user", user);
        Appointment nextApt = checkAptExists(user);
        model.addAttribute("apt", nextApt);
        logger.warn("Editing user information page is accessed of ID: " + userId);

        return "edit_user_info";
    }


    @RequestMapping("/confirmDose1/{user_id}")
    public String confirmDose1(@PathVariable(value = "user_id") Long userId,
                               @RequestParam(value = "vaccine") String vaccine,
                               Model model) {

        User user = userRepository.findByID(userId);
        Appointment attendedApt = user.getNextApptId();
        /* Time organising */
        String oldDate = attendedApt.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = new java.util.Date();

        try {
            newDate = sdf.parse(oldDate);
        } catch (ParseException pe){
            pe.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(newDate);
        c.add(Calendar.DATE, 21); // Adding 3 weeks
        String futureDate = sdf.format(c.getTime());
        System.out.println(futureDate);

        /* Appointment Organising*/
        String confirmedDose = vaccine;
        String attendedDate = attendedApt.getDate();
        System.out.println("Setting user null");
        user.setNextApptId(null);
        System.out.println("Deleting Apt");
        appointmentRepository.delete(attendedApt); // Delete old appointment

        Appointment newApt = new Appointment(
                attendedApt.getVaccine(),
                "dose2",
                futureDate,
                attendedApt.getTime(),
                attendedApt.getVenue()
        );
        System.out.println("Saving New Apt");
        appointmentRepository.save(newApt); // Create new appointment 21 days in future
        System.out.println("Updating user to include new apt");
        user.setNextApptId(newApt);
        System.out.println("Updating dose info");
        userRepository.updateDose1(confirmedDose, userId); // Update dose on users table
        userRepository.updateDose1Date(attendedDate, userId); // Update dose date on users table
        logger.info("User dosage number one confirmed of ID: " + userId);
        return "redirect:/users/editUserInfo/" + userId.toString();
    }

    @RequestMapping("/confirmDose2/{user_id}")
    public String confirmDose2(@PathVariable(value = "user_id") Long userId,
                               @RequestParam(value = "vaccine") String vaccine,
                               Model model) {
        User user = userRepository.findByID(userId);
        Appointment attendedApt = user.getNextApptId();
        String confirmedDose = vaccine;
        String attendedDate = attendedApt.getDate();
        System.out.println("Setting user null");
        user.setNextApptId(null);
        System.out.println("Deleting Apt");
        appointmentRepository.delete(attendedApt); // Delete old appointment
        userRepository.updateDose2(confirmedDose, userId); // Update dose on users table
        userRepository.updateDose2Date(attendedDate, userId); // Update dose date on users table
        logger.info("User dosage number two confirmed of ID: " + userId);
        return "redirect:/users/editUserInfo/" + userId.toString();
    }

    // Get All users
    public List<User> getAllUsers(){
        return  userRepository.findAll();
    }


    // I will try to consolidate this and the email check into one method
    public Boolean getUserByEmail(String email) {
        var users = getAllUsers();

        var user =  users.stream()
                .filter(t -> email.equals(t.getEmail()))
                .findFirst()
                .orElse(null);

        if (user == null) return false;
        else return true;
    }


    public Boolean getUserByPPSN(String ppsn) {
        var users = getAllUsers();

        var user =  users.stream()
                .filter(t -> ppsn.equals(t.getPpsn()))
                .findFirst()
                .orElse(null);

        return user != null;
    }


    @GetMapping("/bookAppointment")
    public String bookingForm(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) throws bookAppointmentException {
        if (userDetails == null) return "login";

        try {
            User user = currentUser(userDetails);
            if (user.getDose2() != null) return "dose3";
            if (user.getNextApptId() != null) return "cancel_first";
            List<String> dates = availableAppointments(user);

            model.addAttribute("test", new VenueAndDate());
            model.addAttribute("venue", new Venue().getId());
            model.addAttribute("availableDates", dates);
            logger.info("Appointment booking page accessed by userID: " + user.getUser_id());
        } catch (Exception e) {
            throw new bookAppointmentException();
        }

        return "select_venue";
    }


    public List<String> availableAppointments(User user) {
        // get today's date
        LocalDate now = LocalDate.now();
        List<String> availableAppointments = new LinkedList<>();
        String[] calendar = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        for (int day = 0; day < 30; day++) {
            List<String> date = List.of(now.plusDays(day).toString().split("-"));
            StringBuilder dateString = new StringBuilder();

            for (int i = 2; i >= 0; i--) {
                String unit = date.get(i);
                if (i == 1) unit = calendar[Integer.parseInt(unit) - 1];
                dateString.append(unit).append(" ");
            }

            availableAppointments.add(dateString.toString());
        }

        System.out.println(availableAppointments);
        return availableAppointments;
    }


    @GetMapping("confirmAppointmentCancellation")
    public String confirmAppointmentCancellation() {
        logger.info("Appointment Cancellation");
        return "confirm_appointment_cancellation";
    }

    @GetMapping("appointmentCancellation")
    public String appointmentCancellation(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        User user = userRepository.findByEmail(userEmail);
        userRepository.cancelUserAppointment(user.getUser_id());
        appointmentRepository.delete(user.getNextApptId());
        logger.info("Appointment Cancellation Confirmed by userID: " + user.getUser_id());
        return "appointment_cancelled";
    }

    public User currentUser(CustomUserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        return userRepository.findByEmail(userEmail);
    }


    public void errorMessage(String errorMessage, Model model) {
        System.out.println(errorMessage);
        model.addAttribute("errorMessage", errorMessage);
        logger.error(errorMessage);
    }

}