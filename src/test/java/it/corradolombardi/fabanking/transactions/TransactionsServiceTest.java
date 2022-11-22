package it.corradolombardi.fabanking.transactions;

import it.corradolombardi.fabanking.model.DateInterval;
import it.corradolombardi.fabanking.model.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static java.time.Month.DECEMBER;
import static java.time.Month.NOVEMBER;

class TransactionsServiceTest {

    private TransactionsService transactionsService;

    private final Random random = new Random();

    @Test
    void noTransactionsReturned() {
        Long accountId = Math.abs(random.nextLong());
        DateInterval dateInterval = DateInterval.of(LocalDate.of(2021, NOVEMBER, 17),
                LocalDate.of(2021, DECEMBER, 17));
        List<Transaction> transactions = transactionsService.transactions(accountId,
                dateInterval);
    }

    // negative account

    // invalid dates

}