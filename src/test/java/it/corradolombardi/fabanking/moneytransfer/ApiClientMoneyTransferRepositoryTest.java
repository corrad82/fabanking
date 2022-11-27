package it.corradolombardi.fabanking.moneytransfer;

import static java.nio.charset.Charset.defaultCharset;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import it.corradolombardi.fabanking.fabrikclient.FabrikAccount;
import it.corradolombardi.fabanking.fabrikclient.FabrikAddress;
import it.corradolombardi.fabanking.fabrikclient.FabrikAmount;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiException;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiStatusCodeException;
import it.corradolombardi.fabanking.fabrikclient.FabrikClient;
import it.corradolombardi.fabanking.fabrikclient.FabrikError;
import it.corradolombardi.fabanking.fabrikclient.FabrikFee;
import it.corradolombardi.fabanking.fabrikclient.FabrikMoneyTransfer;
import it.corradolombardi.fabanking.fabrikclient.FabrikMoneyTransferRequest;
import it.corradolombardi.fabanking.fabrikclient.FabrikPerson;
import it.corradolombardi.fabanking.fabrikclient.MoneyTransferFabrikApiResponse;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.Amount;
import it.corradolombardi.fabanking.model.MoneyTransfer;
import it.corradolombardi.fabanking.model.MoneyTransferException;
import it.corradolombardi.fabanking.model.MoneyTransferRequest;
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
class ApiClientMoneyTransferRepositoryTest {

    private static final Currency EUR = Currency.getInstance("EUR");
    private ApiClientMoneyTransferRepository repository;
    @Mock
    private FabrikClient fabrikClient;

    @BeforeEach
    void setUp() {
        repository = new ApiClientMoneyTransferRepository(fabrikClient);
    }

    @Test
    void clientReturnsData() throws Exception {

        Long accountId = 123L;

        FabrikPerson creditor = new FabrikPerson("john smith",
                                                 new FabrikAccount("", ""),
                                                 new FabrikAddress("", "", ""));

        FabrikPerson debtor = new FabrikPerson("doe",
                                               new FabrikAccount("", ""),
                                               new FabrikAddress("", "", ""));

        when(fabrikClient.moneyTransfer(any(), any()))
            .thenReturn(new MoneyTransferFabrikApiResponse(
                "OK",
                null,
                new FabrikMoneyTransfer("id", "EXECUTED", "OUTGOING",
                                        creditor, debtor, "cro", "trn",
                                        "uri", "Just a brief description",
                                        "2022-11-27T10:38:55.949",
                                        "2022-11-28T10:38:55.949",
                                        "2022-11-28", "2022-11-29",
                                        new FabrikAmount(1d, "EUR", 1d,
                                                         "EUR", "2022-11-29",
                                                         1d),
                                        false, false, "fee", "123",
                                        Collections.singletonList(
                                            new FabrikFee("code", "descr", 1d, "EUR")
                                        ), false)));

        MoneyTransfer moneyTransfer = repository.transfer(new MoneyTransferRequest(
            accountId, "john smith",
            "Just a brief description",

            new MoneyTransferRequest.Amount(100.00, "EUR"),
            "2022-11-28"));


        // only part of the response is checked
        assertThat(moneyTransfer.getDescription(), is("Just a brief description"));
        assertThat(moneyTransfer.getAmounts().getCreditorAmount(), is(new Amount(100L, EUR)));
        assertThat(moneyTransfer.getCreditor().getName(), is("john smith"));
        assertThat(moneyTransfer.getDebtorValueDate(), is(LocalDate.parse("2022-11-28")));
    }

    @Test
    void errorReturnedByClient() throws Exception {
        long accountId = 999L;
//        FabrikMoneyTransferRequest request = FabrikMoneyTransferRequest.builder().build();

        when(fabrikClient.moneyTransfer(eq(accountId), any()))
            .thenReturn(
                new MoneyTransferFabrikApiResponse("KO",
                                                   List.of(
                                                       new FabrikError("777AE", "An error occurred", "params")
                                                   ),
                                                   null)
            );

        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest(accountId,
                                                                             "smith",
                                                                             "description",
                                                                             new MoneyTransferRequest.Amount(100.00, "EUR"),
                                                                             "2022-11-28");
        assertThrows(MoneyTransferException.class,
                     () -> repository.transfer(moneyTransferRequest));
    }

    @Test
    void exceptionThrownByClient() throws Exception {
        long accountId = 999123L;
//        DateInterval dateInterval = DateInterval.of("2022-09-24", "2022-10-16");

        doThrow(new FabrikApiException(new RestClientException("something went wrong")))
            .when(fabrikClient)
            .moneyTransfer(eq(accountId), any());

        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest(accountId,
                                                                             "smith",
                                                                             "description",
                                                                             new MoneyTransferRequest.Amount(100.00, "EUR"),
                                                                             "2022-11-28");
        assertThrows(MoneyTransferException.class,
                     () -> repository.transfer(moneyTransferRequest));
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
        expectStatusCodeException(accountId, payload);

        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest(accountId,
                                                                             "smith",
                                                                             "description",
                                                                             new MoneyTransferRequest.Amount(100.00,
                                                                                                             "EUR"),
                                                                             "2022-11-28");
        assertThrows(AccountNotFoundException.class,
                     () -> repository.transfer(moneyTransferRequest));
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

        expectStatusCodeException(accountId, payload);

        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest(accountId,
                                                                             "smith",
                                                                             "description",
                                                                             new MoneyTransferRequest.Amount(100.00,
                                                                                                             "EUR"),
                                                                             "2022-11-28");
        assertThrows(MoneyTransferException.class,
                     () -> repository.transfer(moneyTransferRequest));
    }

    private void expectStatusCodeException(long accountId, String payload)
        throws FabrikApiException {
        HttpStatusCodeException e = new HttpClientErrorException(
            HttpStatus.FORBIDDEN,
            "Forbidden",
            payload.getBytes(defaultCharset()),
            defaultCharset());


        doThrow(new FabrikApiStatusCodeException(e))
            .when(fabrikClient)
            .moneyTransfer(eq(accountId), any());
    }
}