package app.exception;

public class UserNotFoundException extends Exception{
    private Long userId;
    public UserNotFoundException(Long userId) {
        super(String.format("User is not found with id : '%s'", userId));
    }
}

