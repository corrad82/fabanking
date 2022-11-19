package it.corradolombardi.fabanking;

import it.corradolombardi.fabanking.balance.ApiClientBalanceRepository;
import it.corradolombardi.fabanking.balance.BalanceRepository;
import it.corradolombardi.fabanking.balance.BalanceService;
import it.corradolombardi.fabanking.fabrikclient.FabrikClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public BalanceRepository balanceRepository(FabrikClient fabrikClient) {
        return new ApiClientBalanceRepository(fabrikClient);
    }
    @Bean
    public BalanceService balanceService(BalanceRepository balanceRepository) {
        return new BalanceService(balanceRepository);
    }

}
