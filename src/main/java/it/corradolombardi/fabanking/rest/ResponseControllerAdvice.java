package it.corradolombardi.fabanking.rest;

import it.corradolombardi.fabanking.balance.BalanceUnavailableException;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import lombok.extern.slf4j.Slf4j;
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

    @ExceptionHandler({BalanceUnavailableException.class})
    protected ResponseEntity<Object> balanceNotAvailable(BalanceUnavailableException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.internalServerError()
            .body(exception.getMessage());
    }
}
