package it.corradolombardi.fabanking.balance;

import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.Balance;

public class BalanceService {

    private final BalanceRepository balanceRepository;

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public Balance balance(Long accountId) throws AccountNotFoundException, BalanceUnavailableException {
        // we do assume account not existing when input account is less or equal than 0
        if (accountId <= 0) {
            throw new AccountNotFoundException(accountId);
        }
        return balanceRepository.balance(accountId);
    }

}
