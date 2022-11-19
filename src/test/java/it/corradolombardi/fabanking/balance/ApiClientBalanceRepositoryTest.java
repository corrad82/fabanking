package it.corradolombardi.fabanking.balance;

import it.corradolombardi.fabanking.fabrikclient.*;
import it.corradolombardi.fabanking.model.Amount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void valueReturnedByClient() throws FabrikApiException {
        long accountId = 1234567L;

        when(fabrikClient.balance(accountId))
                .thenReturn(
                        new BalancecFabrikResponse("OK",
                                null,
                                new FabrikBalance("2022-11-19", "100.00", "99.00", "USD"))
                );

        Optional<Balance> balance = apiClientBalanceRepository.balance(accountId);


        Balance expectedBalance = new Balance(LocalDate.of(2022, 11, 19),
                new Amount(9900L, USD),
                new Amount(10000L, USD));
        assertEquals(Optional.of(expectedBalance), balance);
    }

    @Test
    void errorReturnedByClient() throws FabrikApiException {
        long accountId = 999L;

        when(fabrikClient.balance(accountId))
                .thenReturn(
                        new BalancecFabrikResponse("KO",
                                Arrays.asList(
                                        new FabrikError("777AE", "An error occurred", "params")
                                ),
                                null)
                );

        Optional<Balance> balance = apiClientBalanceRepository.balance(accountId);


        assertEquals(Optional.empty(), balance);
    }

    @Test
    void exceptionThrownByClient() throws FabrikApiException {
        long accountId = 999L;

        doThrow(new FabrikApiException(new Exception("something went wrong")))
                .when(fabrikClient)
                .balance(accountId);

        Optional<Balance> balance = apiClientBalanceRepository.balance(accountId);


        assertEquals(Optional.empty(), balance);
    }
}