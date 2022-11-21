package it.corradolombardi.fabanking.balance;

import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.Balance;

public interface BalanceRepository {
    Balance balance(Long accountId) throws AccountNotFoundException, BalanceUnavailableException;
}
