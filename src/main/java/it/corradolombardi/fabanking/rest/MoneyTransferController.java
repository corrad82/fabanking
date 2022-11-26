package it.corradolombardi.fabanking.rest;

import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.MoneyTransferException;
import it.corradolombardi.fabanking.model.MoneyTransferRequest;
import it.corradolombardi.fabanking.moneytransfer.MoneyTransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/money-transfer")
public class MoneyTransferController {

    private final MoneyTransferService moneyTransferService;

    public MoneyTransferController(MoneyTransferService moneyTransferService) {
        this.moneyTransferService = moneyTransferService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<MoneyTransferRest> transfer(@RequestBody MoneyTransferRequest moneyTransferRequest)
        throws MoneyTransferException, AccountNotFoundException {

        return ResponseEntity.ok(moneyTransferService.transfer(moneyTransferRequest)
                                     .toRest());
    }
}
