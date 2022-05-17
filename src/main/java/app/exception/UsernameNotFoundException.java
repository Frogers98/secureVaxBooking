package app.exception;

public class UsernameNotFoundException extends Exception{
    private String username;
    public UsernameNotFoundException(String username) {
        super("User is not found with username: " + username);
    }
}

