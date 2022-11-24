package it.corradolombardi.fabanking.rest;

import com.google.gson.JsonObject;
import it.corradolombardi.fabanking.balance.InformationUnavailableException;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
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

    @ExceptionHandler({InformationUnavailableException.class})
    protected ResponseEntity<String> informationNotAvailable(InformationUnavailableException exception) {
        log.error(exception.getMessage(), exception);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("error", exception.getMessage());
        return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON)
            .body(jsonObject.toString());
    }




}
