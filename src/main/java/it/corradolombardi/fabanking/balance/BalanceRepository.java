package it.corradolombardi.fabanking.balance;

import it.corradolombardi.fabanking.model.AccountNotFoundException;

public interface BalanceRepository {
    Balance balance(Long accountId) throws AccountNotFoundException, BalanceUnavailableException;
}
