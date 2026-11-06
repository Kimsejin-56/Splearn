package spring.splearn.adapter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import spring.splearn.domain.member.DuplicateEmailException;
import spring.splearn.domain.member.DuplicateProfileException;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e) {
        return getProblemDetail(INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler({DuplicateEmailException.class, DuplicateProfileException.class})
    public ProblemDetail emailExceptionHandler(DuplicateEmailException e) {
        return getProblemDetail(CONFLICT, e);
    }

    private static ProblemDetail getProblemDetail(HttpStatus status, Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());

        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("exception", ex.getClass().getSimpleName());

        return problemDetail;
    }
}
