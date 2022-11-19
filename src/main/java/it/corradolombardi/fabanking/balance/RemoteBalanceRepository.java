package it.corradolombardi.fabanking.balance;

import java.util.Optional;

public class RemoteBalanceRepository implements BalanceRepository {
    @Override
    public Optional<Balance> balance(Long accountId) {
        return Optional.empty();
    }
}
