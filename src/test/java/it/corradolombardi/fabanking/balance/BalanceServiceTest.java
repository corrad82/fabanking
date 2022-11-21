package it.corradolombardi.fabanking.balance;

import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.Amount;
import it.corradolombardi.fabanking.model.Balance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.negateExact;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    public static final Currency EUR = Currency.getInstance("EUR");
    private BalanceService balanceService;
    @Mock
    private BalanceRepository balanceRepository;

    private final Random random = new Random();

    private Long accountId;

    @BeforeEach
    public void setUp() {
        balanceService = new BalanceService(balanceRepository);
        accountId = abs(random.nextLong());
    }

    @Test
    public void positiveBalance() throws BalanceUnavailableException, AccountNotFoundException {

        LocalDate date = LocalDate.now();
        Amount availableBalance = new Amount(abs(random.nextLong()), EUR);
        Amount balance = new Amount(abs(random.nextLong()), EUR);

        when(balanceRepository.balance(accountId))
            .thenReturn(new Balance(date, availableBalance, balance));

        Balance result = balanceService.balance(accountId);

        assertThat(result, is(
            new Balance(date,
                        availableBalance,
                        balance)
        ));
    }

    @Test
    public void negativeBalance() throws BalanceUnavailableException, AccountNotFoundException {
        LocalDate date = LocalDate.now();
        Amount availableBalance = new Amount(negate(random.nextLong()), EUR);
        Amount balance = new Amount(negate(random.nextLong()), EUR);

        when(balanceRepository.balance(accountId))
            .thenReturn(new Balance(date, availableBalance, balance));

        Balance result = balanceService.balance(accountId);

        assertThat(result.getAvailableBalance().getCents(), lessThan(0L));
        assertThat(result.getBalance().getCents(), lessThan(0L));
    }

    @Test
    public void exceptionRethrown() throws AccountNotFoundException, BalanceUnavailableException {

        doThrow(new BalanceUnavailableException()).
            when(balanceRepository)
            .balance(accountId);

        try {
            balanceService.balance(accountId);
        } catch (BalanceUnavailableException e) {
            assertThat(e.getMessage(), is("Unable to find balance"));
        }
    }

    @Test
    public void accountNotFound() throws BalanceUnavailableException {

        try {
            balanceService.balance(-10L);
        } catch (AccountNotFoundException e) {
            assertThat(e.getMessage(), is("Account with id -10 has not been found"));
        }
    }

    private long negate(long l) {
        return l < 0 ? l : negateExact(l);
    }

}