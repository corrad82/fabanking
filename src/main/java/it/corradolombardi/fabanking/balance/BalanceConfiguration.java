package it.corradolombardi.fabanking.balance;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BalanceConfiguration {

    @Bean
    public BalanceService balanceService() {
        return new BalanceService(null);
    }
}
