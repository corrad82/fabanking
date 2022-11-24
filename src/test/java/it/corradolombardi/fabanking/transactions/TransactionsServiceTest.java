package it.corradolombardi.fabanking.transactions;

import it.corradolombardi.fabanking.balance.InformationUnavailableException;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.Amount;
import it.corradolombardi.fabanking.model.DateInterval;
import it.corradolombardi.fabanking.model.Transaction;
import it.corradolombardi.fabanking.model.Transaction.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;
import static java.time.LocalDate.parse;
import static java.time.Month.DECEMBER;
import static java.time.Month.NOVEMBER;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionsServiceTest {


    private static final Currency EUR = Currency.getInstance("EUR");
    private final Random random = new Random();
    private TransactionsService transactionsService;
    @Mock
    private TransactionsRepository transactionsRepository;
    private Long accountId;


    @BeforeEach
    void setUp() {
        transactionsService = new TransactionsService(transactionsRepository);
        accountId = abs(random.nextLong());
    }

    @Test
    void noTransactionsReturned() throws Exception {

        DateInterval dateInterval = DateInterval.of(LocalDate.of(2021, NOVEMBER, 17),
                                                    LocalDate.of(2021, DECEMBER, 17));
        when(transactionsRepository.transactions(accountId, dateInterval))
            .thenReturn(emptyList());
        List<Transaction> transactions = transactionsService.transactions(accountId,
                                                                          dateInterval);
        assertThat(transactions, is(emptyList()));
    }


    @Test
    void negativeAccountIdThrowsException() {

        Long accountId = -10L;
        DateInterval dateInterval = DateInterval.of(LocalDate.of(2021, NOVEMBER, 17),
                                                    LocalDate.of(2021, DECEMBER, 17));

        assertThrows(AccountNotFoundException.class,
                     () -> transactionsService.transactions(accountId,
                                                            dateInterval),
                     "Unable to find account with id: -10");
        verifyNoInteractions(transactionsRepository);
    }

    @Test
    void futureDatesThrowIllegalArgumentException() {

        DateInterval dateInterval = DateInterval.of(LocalDate.now().plusDays(1),
                                                    LocalDate.now().plusMonths(1));

        assertThrows(IllegalArgumentException.class,
                     () -> transactionsService.transactions(accountId,
                                                            dateInterval));
        verifyNoInteractions(transactionsRepository);
    }

    @Test
    void invalidDatesThrowIllegalArgumentException() {

        DateInterval dateInterval = DateInterval.of(LocalDate.of(2021, DECEMBER, 25),
                                                    LocalDate.of(2021, DECEMBER, 17));

        assertThrows(IllegalArgumentException.class,
                     () -> transactionsService.transactions(accountId,
                                                            dateInterval));
        verifyNoInteractions(transactionsRepository);
    }

    @Test
    void transactionsReturned() throws Exception {

        DateInterval dateInterval = DateInterval.of(LocalDate.of(2021, NOVEMBER, 17),
                                                    LocalDate.of(2021, DECEMBER, 17));

        when(transactionsRepository.transactions(accountId, dateInterval))
            .thenReturn(List.of(
                transaction("12345",
                            "0004545",
                            new TransactionType("typeenum", "typevalue"),
                            "2021-11-23",
                            "2021-11-25",
                            -23000L,
                            "test description for transaction 1"),
                transaction("9876",
                            "00043434",
                            new TransactionType("typeenum2", "typevalue2"),
                            "2021-11-12",
                            "2021-11-10",
                            120000L,
                            "salary")
            ));

        List<Transaction> transactions = transactionsService.transactions(accountId,
                                                                          dateInterval);
        List<Transaction> expectedTransactions = List.of(
            transaction("12345",
                        "0004545",
                        new TransactionType("typeenum", "typevalue"),
                        "2021-11-23",
                        "2021-11-25",
                        -23000L,
                        "test description for transaction 1"),
            transaction("9876",
                        "00043434",
                        new TransactionType("typeenum2", "typevalue2"),
                        "2021-11-12",
                        "2021-11-10",
                        120000L,
                        "salary")
        );
        assertThat(transactions, is(expectedTransactions));
    }

    @Test
    void accountNotFoundExceptionFromRepository() throws Exception {

        DateInterval dateInterval = DateInterval.of(LocalDate.of(2021, NOVEMBER, 17),
                                                    LocalDate.of(2021, DECEMBER, 17));

        doThrow(new AccountNotFoundException(accountId))
            .when(transactionsRepository)
            .transactions(accountId, dateInterval);

        assertThrows(AccountNotFoundException.class,
                     () -> transactionsService.transactions(accountId, dateInterval));
    }

    @Test
    void informationUnavailableExceptionFromRepository() throws Exception {

        DateInterval dateInterval = DateInterval.of(LocalDate.of(2021, NOVEMBER, 17),
                                                    LocalDate.of(2021, DECEMBER, 17));

        doThrow(new InformationUnavailableException(accountId, dateInterval))
            .when(transactionsRepository)
            .transactions(accountId, dateInterval);

        assertThrows(InformationUnavailableException.class,
                     () -> transactionsService.transactions(accountId, dateInterval));
    }

    private Transaction transaction(String transactionId, String operationId, TransactionType transactionType,
                                    String accountingDate, String valueDate, long amountCents, String description) {
        return Transaction.builder()
                          .transactionId(transactionId)
                          .operationId(operationId)
                          .transactionType(transactionType)
                          .accountingDate(parse(accountingDate))
                          .valueDate(parse(valueDate))
                          .amount(new Amount(amountCents, EUR))
                          .description(description)
                          .build();
    }


}