package app.exception;

public class PostNotFoundException extends Exception{
    private Long userId;
    public PostNotFoundException(Long postId) {
        super(String.format("Post is not found with id : '%s'", postId));
    }
}
