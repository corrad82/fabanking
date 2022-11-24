package it.corradolombardi.fabanking.rest;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


import it.corradolombardi.fabanking.balance.InformationUnavailableException;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.DateInterval;
import it.corradolombardi.fabanking.model.Transaction;
import it.corradolombardi.fabanking.transactions.TransactionsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionsController {

    private final TransactionsService transactionsService;

    @GetMapping("/transactions/{accountId}")
    public ResponseEntity<List<TransactionRest>> transactions(@PathVariable("accountId") Long accountId,
                                                              @RequestParam("fromAccountingDate") String dateFrom,
                                                              @RequestParam("toAccountingDate") String dateTo)
        throws InformationUnavailableException, AccountNotFoundException {


        List<Transaction> transactions = transactionsService.transactions(
            accountId,
            dateInterval(dateFrom, dateTo));

        return ResponseEntity.ok(transactions
                                     .stream()
                                     .map(Transaction::toRest)
                                     .collect(toList()));

    }

    private DateInterval dateInterval(String dateFrom, String dateTo) {
        LocalDate to = null;
        LocalDate from = null;
        if (StringUtils.isNotBlank(dateFrom)) {
            from = LocalDate.parse(dateFrom);
        }
        if (StringUtils.isNotBlank(dateTo)) {
            to = LocalDate.parse(dateTo);
        }
        if (Objects.isNull(to)) {
            to = Optional.ofNullable(from).map(ld -> ld.plusMonths(1L))
                         .orElseGet(LocalDate::now);
        }
        if (Objects.isNull(from)) {
            from = to.minusMonths(1L);
        }

        return DateInterval.of(from, to);
    }
}
