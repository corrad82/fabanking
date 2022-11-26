package it.corradolombardi.fabanking.transactions;

import static java.nio.charset.Charset.defaultCharset;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import it.corradolombardi.fabanking.model.InformationUnavailableException;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiException;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiStatusCodeException;
import it.corradolombardi.fabanking.fabrikclient.FabrikClient;
import it.corradolombardi.fabanking.fabrikclient.FabrikError;
import it.corradolombardi.fabanking.fabrikclient.FabrikTransaction;
import it.corradolombardi.fabanking.fabrikclient.FabrikTransactionsList;
import it.corradolombardi.fabanking.fabrikclient.TransactionsFabrikResponse;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.Amount;
import it.corradolombardi.fabanking.model.DateInterval;
import it.corradolombardi.fabanking.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

@ExtendWith(MockitoExtension.class)
class ApiClientTransactionsRepositoryTest {


    private static final Currency EUR = Currency.getInstance("EUR");
    @Mock
    private FabrikClient fabrikClient;
    private ApiClientTransactionsRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ApiClientTransactionsRepository(fabrikClient);
    }

    @Test
    void clientReturnsData() throws Exception {

        Long accountId = 123L;
        DateInterval dateInterval = DateInterval.of("2022-11-10", "2022-11-24");

        when(fabrikClient.transactions(accountId, dateInterval))
            .thenReturn(new TransactionsFabrikResponse(
                "OK",
                null,
                new FabrikTransactionsList(
                    List.of(
                        new FabrikTransaction("123", "555", "2022-11-11", "2022-11-12",
                                              new FabrikTransaction.FabrikTransactionType("enum", "value"),
                                              "-250.00", "EUR", "transaction 1 description"),
                        new FabrikTransaction("555", "23423", "2022-11-15", "2022-11-19",
                                              new FabrikTransaction.FabrikTransactionType("enum", "value"),
                                              "1250.00", "EUR", "transaction 2 description")
                    )
                )
            ));

        List<Transaction> transactions = repository.transactions(accountId, dateInterval);

        List<Transaction> expectedTransactions = List.of(
            transaction("123", "555", "2022-11-11", "2022-11-12",
                        "enum", "value", -25000L, "transaction 1 description"),
            transaction("555", "23423", "2022-11-15", "2022-11-19",
                        "enum", "value", 125000L, "transaction 2 description")
        );
        assertThat(transactions, is(expectedTransactions));
    }

    @Test
    void errorReturnedByClient() throws Exception {
        long accountId = 999L;
        DateInterval dateInterval = DateInterval.of("2022-10-31", "2022-11-05");

        when(fabrikClient.transactions(accountId, dateInterval))
            .thenReturn(
                new TransactionsFabrikResponse("KO",
                                           List.of(
                                               new FabrikError("777AE", "An error occurred", "params")
                                           ),
                                           null)
            );

        assertThrows(InformationUnavailableException.class,
                     () -> repository.transactions(accountId, dateInterval));
    }

    @Test
    void exceptionThrownByClient() throws Exception {
        long accountId = 999123L;
        DateInterval dateInterval = DateInterval.of("2022-09-24", "2022-10-16");

        doThrow(new FabrikApiException(new RestClientException("something went wrong")))
            .when(fabrikClient)
            .transactions(accountId, dateInterval);

        assertThrows(InformationUnavailableException.class,
                     () -> repository.transactions(accountId, dateInterval));
    }

    @Test
    void invalidAccountThrowsAccountNotFound() throws Exception {

        String payload = "{" +
            "    \"status\": \"KO\"," +
            "    \"errors\": [" +
            "        {" +
            "            \"code\": \"REQ004\"," +
            "            \"description\": \"Invalid account identifier\"," +
            "            \"params\": \"\"" +
            "        }" +
            "    ]," +
            "    \"payload\": {}" +
            "}";

        long accountId = 45235L;
        DateInterval dateInterval = DateInterval.of("2022-09-11", "2022-10-10");
        expectStatusCodeException(accountId, payload, dateInterval);

        assertThrows(AccountNotFoundException.class,
                     () -> repository.transactions(accountId, dateInterval));
    }

    @Test
    void otherForbiddenErrorMessageThrowsBalanceNotFound() throws Exception {
        String payload = "{" +
            "    \"status\": \"KO\"," +
            "    \"errors\": [" +
            "        {" +
            "            \"code\": \"REQ003\"," +
            "            \"description\": \"Missing request header: Auth-Schema\"," +
            "            \"params\": \"\"" +
            "        }" +
            "    ]," +
            "    \"payload\": {}" +
            "}";

        long accountId = 45235L;

        DateInterval dateInterval = DateInterval.of("2022-08-26", "2022-09-24");
        expectStatusCodeException(accountId, payload, dateInterval);

        assertThrows(InformationUnavailableException.class,
                     () -> repository.transactions(accountId, dateInterval));
    }

    private void expectStatusCodeException(long accountId, String payload, DateInterval dateInterval) throws FabrikApiException {
        HttpStatusCodeException e = new HttpClientErrorException(
            HttpStatus.FORBIDDEN,
            "Forbidden",
            payload.getBytes(defaultCharset()),
            defaultCharset());


        doThrow(new FabrikApiStatusCodeException(e))
            .when(fabrikClient)
            .transactions(accountId, dateInterval);
    }

    private Transaction transaction(String transactionId, String operationId, String accountingDate, String valueDate,
                                    String typeEnum, String typeValue, long amountCents, String description) {
        return Transaction
            .builder()
            .transactionId(transactionId)
            .operationId(operationId)
            .accountingDate(LocalDate.parse(accountingDate))
            .valueDate(LocalDate.parse(valueDate))
            .transactionType(new Transaction.TransactionType(typeEnum, typeValue))
            .amount(new Amount(amountCents, EUR))
            .description(description)
            .build();
    }
}