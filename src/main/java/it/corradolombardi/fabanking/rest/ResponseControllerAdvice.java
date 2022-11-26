package it.corradolombardi.fabanking.rest;

import java.time.format.DateTimeParseException;

import com.google.gson.JsonObject;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.InformationUnavailableException;
import it.corradolombardi.fabanking.model.MoneyTransferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ResponseControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    protected ResponseEntity<Object> accountNotFound(AccountNotFoundException exception) {
        log.warn(exception.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DateTimeParseException.class)
    protected ResponseEntity<String> dateFormat(DateTimeParseException exception) {
        log.warn(exception.getMessage());
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                             .body(toResponseBody("requested date cannot be processed"));
    }

    @ExceptionHandler({InformationUnavailableException.class, MoneyTransferException.class})
    protected ResponseEntity<String> informationNotAvailable(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON)
                             .body(toResponseBody(exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<String> illegalArgumen(IllegalArgumentException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
            .body(toResponseBody(exception.getMessage()));
    }

    private static String toResponseBody(String errorMessage) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("error", errorMessage);
        return jsonObject.toString();
    }


}
