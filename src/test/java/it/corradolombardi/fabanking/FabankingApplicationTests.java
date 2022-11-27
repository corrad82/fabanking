package it.corradolombardi.fabanking;

import it.corradolombardi.fabanking.rest.BalanceController;
import it.corradolombardi.fabanking.rest.MoneyTransferController;
import it.corradolombardi.fabanking.rest.TransactionsController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

@SpringBootTest
class FabankingApplicationTests {

    @Autowired
    private BalanceController balanceController;
    @Autowired
    private TransactionsController transactionsController;
    @Autowired
    private MoneyTransferController moneyTransferController;

    @Test
    public void contextLoaded() {
        assertThat(balanceController, is(notNullValue()));
        assertThat(transactionsController, is(notNullValue()));
        assertThat(moneyTransferController, is(notNullValue()));
    }

}
