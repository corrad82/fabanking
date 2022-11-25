package it.corradolombardi.fabanking.rest;

import it.corradolombardi.fabanking.model.Balance;
import it.corradolombardi.fabanking.balance.BalanceService;
import it.corradolombardi.fabanking.balance.InformationUnavailableException;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("{accountId}")
    public ResponseEntity<BalanceRest> balance(@PathVariable("accountId") Long accountId)
        throws InformationUnavailableException, AccountNotFoundException {

        Balance balance = balanceService.balance(accountId);
        BalanceRest body = balance.toRest();
        return ResponseEntity.ok(body);

    }

}
