package app.controller;

import app.model.Venue;
import app.repository.VenueRepository;
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
@RequestMapping(path = "venues")
public class VenueController {

    @Autowired
    VenueRepository venueRepository;

    @GetMapping("")
    public String listUsers(Model model) {
        List<Venue> allVenues = getAllVenues();
        model.addAttribute("listVenues", allVenues);
        return "list_venues";
    }

    @PostMapping
    public void newVenue(@Valid @RequestBody Venue newVenue) {
        if (getVenueByName(newVenue.getVenue_name()))
            System.out.println("Venue already in database");
        else venueRepository.save(newVenue);
    }

    public Boolean getVenueByName(String venueName) {
        var venues = getAllVenues();

        var venue =  venues.stream()
                .filter(t -> venueName.equals(t.getVenue_name()))
                .findFirst()
                .orElse(null);

        if (venue == null) return false;
        else return true;
    }

    // Get All venues
    public List<Venue> getAllVenues(){
        return venueRepository.findAll();
    }
}
