package it.corradolombardi.fabanking.transactions;

import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.DateInterval;
import it.corradolombardi.fabanking.model.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;
import static java.time.Month.DECEMBER;
import static java.time.Month.NOVEMBER;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class TransactionsServiceTest {


    private final Random random = new Random();
    private TransactionsService transactionsService;


    @BeforeEach
    void setUp() {
        transactionsService = new TransactionsService();
    }

    @Test
    void noTransactionsReturned() throws Exception {
        Long accountId = abs(random.nextLong());
        DateInterval dateInterval = DateInterval.of(LocalDate.of(2021, NOVEMBER, 17),
                LocalDate.of(2021, DECEMBER, 17));
        List<Transaction> transactions = transactionsService.transactions(accountId,
                dateInterval);
        assertThat(transactions, is(emptyList()));
    }

    // negative account

    @Test
    void negativeAccountIdThrowsException() {
        Long accountId = negate(random.nextLong());
        DateInterval dateInterval = DateInterval.of(LocalDate.of(2021, NOVEMBER, 17),
                LocalDate.of(2021, DECEMBER, 17));

        Assertions.assertThrows(AccountNotFoundException.class,
                () -> transactionsService.transactions(accountId,
                        dateInterval));
    }

    @Test
    void futureDatesThrowIllegalArgumentException() {
        Long accountId = abs(random.nextLong());
        DateInterval dateInterval = DateInterval.of(LocalDate.now().plusDays(1),
                LocalDate.now().plusMonths(1));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> transactionsService.transactions(accountId,
                        dateInterval));
    }

    @Test
    void invalidDatesThrowIllegalArgumentException() {
        Long accountId = abs(random.nextLong());
        DateInterval dateInterval = DateInterval.of(LocalDate.of(2021, DECEMBER, 25),
                LocalDate.of(2021, DECEMBER, 17));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> transactionsService.transactions(accountId,
                        dateInterval));
    }

    private Long negate(long value) {
        return value < 0 ? value : Math.negateExact(value);
    }


    // invalid dates

}