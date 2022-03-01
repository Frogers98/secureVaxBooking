package app.exception;

// Exception to be returned when a reply cannot be found by its reply_id (PK)
public class ReplyNotFoundExceptionPK extends Exception{
    private Long userId;
    public ReplyNotFoundExceptionPK(Long replyId) {
        super(String.format("Reply is not found with reply_id : '%s'", replyId));
    }
}
