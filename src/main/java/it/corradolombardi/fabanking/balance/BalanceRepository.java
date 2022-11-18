package it.corradolombardi.fabanking.balance;

import java.util.Optional;

public interface BalanceRepository {
    Optional<Balance> balance(Long accountId);
}
