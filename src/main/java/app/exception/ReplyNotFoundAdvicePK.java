package app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ReplyNotFoundAdvicePK {
    @ResponseBody
    @ExceptionHandler(ReplyNotFoundExceptionPK.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String postNotFoundHandler(ReplyNotFoundExceptionPK ex) {
        return ex.getMessage();
    }
}
