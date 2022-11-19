package it.corradolombardi.fabanking;

import it.corradolombardi.fabanking.rest.BalanceController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;


@SpringBootTest
public class FabankingSmokeTests {

    @Autowired
    private BalanceController balanceController;

    @Test
    public void contextLoaded() {
        assertThat(balanceController, is(notNullValue()));
    }
}
