package it.corradolombardi.fabanking.balance;

import it.corradolombardi.fabanking.fabrikclient.*;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.Amount;
import it.corradolombardi.fabanking.model.Balance;
import it.corradolombardi.fabanking.model.InformationUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import static java.nio.charset.Charset.defaultCharset;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiClientBalanceRepositoryTest {


    public static final Currency USD = Currency.getInstance("USD");
    @Mock
    private FabrikClient fabrikClient;
    private ApiClientBalanceRepository apiClientBalanceRepository;

    @BeforeEach
    void setUp() {
        apiClientBalanceRepository = new ApiClientBalanceRepository(fabrikClient);
    }

    @Test
    void valueReturnedByClient() throws Exception {
        long accountId = 1234567L;

        when(fabrikClient.balance(accountId))
            .thenReturn(
                new BalancecFabrikApiResponse("OK",
                                              null,
                                              new FabrikBalance("2022-11-19", "100.00", "99.00", "USD"))
            );

        Balance balance = apiClientBalanceRepository.balance(accountId);


        Balance expectedBalance = new Balance(LocalDate.of(2022, 11, 19),
                                              new Amount(9900L, USD),
                                              new Amount(10000L, USD));
        assertEquals(expectedBalance, balance);
    }

    @Test
    void errorReturnedByClient() throws Exception {
        long accountId = 999L;

        when(fabrikClient.balance(accountId))
            .thenReturn(
                new BalancecFabrikApiResponse("KO",
                                              List.of(
                                               new FabrikError("777AE", "An error occurred", "params")
                                           ),
                                              null)
            );

        assertThrows(InformationUnavailableException.class,
                     () -> apiClientBalanceRepository.balance(accountId));
    }

    @Test
    void exceptionThrownByClient() throws Exception {
        long accountId = 999L;

        doThrow(new FabrikApiException(new RestClientException("something went wrong")))
            .when(fabrikClient)
            .balance(accountId);

        assertThrows(InformationUnavailableException.class,
                     () -> apiClientBalanceRepository.balance(accountId));
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

        assertThrows(AccountNotFoundException.class,
                     () -> apiClientBalanceRepository.balance(accountId));
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

        assertThrows(InformationUnavailableException.class,
                     () -> apiClientBalanceRepository.balance(accountId));
    }

    private void expectStatusCodeException(long accountId, String payload) throws FabrikApiException {
        HttpStatusCodeException e = new HttpClientErrorException(
            HttpStatus.FORBIDDEN,
            "Forbidden",
            payload.getBytes(defaultCharset()),
            defaultCharset());


        doThrow(new FabrikApiStatusCodeException(e))
            .when(fabrikClient)
            .balance(accountId);
    }
}