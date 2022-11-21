package it.corradolombardi.fabanking.balance;

import it.corradolombardi.fabanking.model.AccountNotFoundException;

import java.util.Optional;

public interface BalanceRepository {
    Optional<Balance> balance(Long accountId) throws AccountNotFoundException, BalanceService.BalanceUnavailableException;
}
