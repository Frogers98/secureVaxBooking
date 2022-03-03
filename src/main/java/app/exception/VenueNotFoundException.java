package app.exception;

public class VenueNotFoundException extends Exception {
    private Long userId;
    public VenueNotFoundException(Long venueId) {

        super(String.format("Venue is not found with id : '%s'", venueId));
    }
}