package it.corradolombardi.fabanking.rest;

import static java.util.stream.Collectors.toList;

import java.util.List;


import it.corradolombardi.fabanking.balance.InformationUnavailableException;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiException;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.DateInterval;
import it.corradolombardi.fabanking.model.Transaction;
import it.corradolombardi.fabanking.transactions.TransactionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    private final TransactionsService transactionsService;

    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @GetMapping("{accountId}")
    public ResponseEntity<List<TransactionRest>> transactions(@PathVariable("accountId") Long accountId,
                                                              @RequestParam("fromAccountingDate") String dateFrom,
                                                              @RequestParam("toAccountingDate") String dateTo)
        throws InformationUnavailableException, AccountNotFoundException, FabrikApiException {


        List<Transaction> transactions = transactionsService.transactions(
            accountId,
            DateInterval.of(dateFrom, dateTo));

        return ResponseEntity.ok(transactions
                                     .stream()
                                     .map(Transaction::toRest)
                                     .collect(toList()));

    }

}
