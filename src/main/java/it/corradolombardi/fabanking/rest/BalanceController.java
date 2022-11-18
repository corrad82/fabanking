package it.corradolombardi.fabanking.rest;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import it.corradolombardi.fabanking.balance.Balance;
import it.corradolombardi.fabanking.balance.BalanceService;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.Amount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BalanceController {

    private final BalanceService balanceService = new BalanceService(
        accountId -> Optional.of(
            new Balance(LocalDate.now(),
                        new Amount(10000L, Currency.getInstance("EUR")),
                        new Amount(12000L, Currency.getInstance("EUR"))
            )
        )
    );

    @GetMapping("/balance/{accountId}")
    public ResponseEntity<Balance> balance(@PathVariable("accountId") Long accountId) {

        try {
            return ResponseEntity.ok(balanceService.balance(accountId));
        } catch (AccountNotFoundException e) {
            log.warn(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (BalanceService.BalanceUnavailableException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
