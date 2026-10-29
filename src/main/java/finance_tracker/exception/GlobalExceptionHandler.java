package finance_tracker.exception;

import finance_tracker.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ) {

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                exception.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND
        );
    }


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailExists(
            EmailAlreadyExistsException exception,
            HttpServletRequest request
    ) {

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                exception.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();


        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                message,
                request.getRequestURI()
        );


        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }

}