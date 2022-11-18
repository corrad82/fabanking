package it.corradolombardi.fabanking.rest;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import it.corradolombardi.fabanking.balance.Balance;
import it.corradolombardi.fabanking.balance.BalanceService;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.Amount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/balance/{accountId}")
    public ResponseEntity<BalanceRest> balance(@PathVariable("accountId") Long accountId) {

        try {
            return ResponseEntity.ok(balanceService.balance(accountId).toRest());
        } catch (AccountNotFoundException e) {
            log.warn(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (BalanceService.BalanceUnavailableException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
