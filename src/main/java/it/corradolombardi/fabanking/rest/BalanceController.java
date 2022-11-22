package it.corradolombardi.fabanking.rest;

import it.corradolombardi.fabanking.model.Balance;
import it.corradolombardi.fabanking.balance.BalanceService;
import it.corradolombardi.fabanking.balance.InformationUnavailableException;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.BalanceRest;
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
    public ResponseEntity<BalanceRest> balance(@PathVariable("accountId") Long accountId)
        throws InformationUnavailableException, AccountNotFoundException {

        Balance balance = balanceService.balance(accountId);
        BalanceRest body = balance.toRest();
        return ResponseEntity.ok(body);

    }

}
