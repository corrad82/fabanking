package it.corradolombardi.fabanking.balance;

import static java.lang.Math.abs;
import static java.lang.Math.negateExact;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;
import java.util.Random;

import it.corradolombardi.fabanking.balance.BalanceService.BalanceUnavailableException;
import it.corradolombardi.fabanking.model.Amount;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BalanceServiceTest {

    public static final Currency EUR = Currency.getInstance("EUR");
    private BalanceService balanceService;
    @Mock
    private BalanceRepository balanceRepository;

    private final Random random = new Random();

    private Long accountId;

    @Before
    public void setUp() {
        balanceService = new BalanceService(balanceRepository);
        accountId = abs(random.nextLong());
    }

    @Test
    public void positiveBalance() {

        LocalDate date = LocalDate.now();
        Amount availableBalance = new Amount(abs(random.nextLong()), EUR);
        Amount balance = new Amount(abs(random.nextLong()), EUR);

        when(balanceRepository.balance(accountId))
            .thenReturn(Optional.of(new Balance(date, availableBalance, balance)));

        Balance result = balanceService.balance(accountId);

        assertThat(result, is(
            new Balance(date,
                        availableBalance,
                        balance)
        ));
    }

    @Test
    public void negativeBalance() {
        LocalDate date = LocalDate.now();
        Amount availableBalance = new Amount(negate(random.nextLong()), EUR);
        Amount balance = new Amount(negate(random.nextLong()), EUR);

        when(balanceRepository.balance(accountId))
            .thenReturn(Optional.of(new Balance(date, availableBalance, balance)));

        Balance result = balanceService.balance(accountId);

        assertThat(result.getAvailableBalance().getCents(), lessThan(0L));
        assertThat(result.getBalance().getCents(), lessThan(0L));
    }

    @Test(expected = BalanceUnavailableException.class)
    public void noBalanceReturnedThrowsException() {

        when(balanceRepository.balance(accountId)).thenReturn(Optional.empty());

        balanceService.balance(accountId);
    }

    private long negate(long l) {
        return l < 0 ? l : negateExact(l);
    }

}