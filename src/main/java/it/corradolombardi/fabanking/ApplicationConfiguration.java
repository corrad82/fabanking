package it.corradolombardi.fabanking;

import it.corradolombardi.fabanking.balance.ApiClientBalanceRepository;
import it.corradolombardi.fabanking.balance.BalanceRepository;
import it.corradolombardi.fabanking.balance.BalanceService;
import it.corradolombardi.fabanking.fabrikclient.FabrikClient;
import it.corradolombardi.fabanking.transactions.ApiClientTransactionsRepository;
import it.corradolombardi.fabanking.transactions.TransactionsRepository;
import it.corradolombardi.fabanking.transactions.TransactionsService;
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

    @Bean
    public TransactionsRepository transactionsRepository(FabrikClient fabrikClient) {
        return new ApiClientTransactionsRepository(fabrikClient);
    }

    @Bean
    public TransactionsService transactionsService(TransactionsRepository transactionsRepository) {
        return new TransactionsService(transactionsRepository);
    }

}
