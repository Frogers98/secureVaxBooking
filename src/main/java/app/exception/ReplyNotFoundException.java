package app.exception;

// Exception to be returned when a reply cannot be found by its post id
public class ReplyNotFoundException extends Exception{
    private Long userId;
    public ReplyNotFoundException(Long postId) {
        super(String.format("Reply is not found with post_id : '%s'", postId));
    }
}
