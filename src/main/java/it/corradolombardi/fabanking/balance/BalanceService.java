package it.corradolombardi.fabanking.balance;

public class BalanceService {

    private final BalanceRepository balanceRepository;

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public Balance balance(Long accountId) {
        return balanceRepository.balance(accountId)
            .orElseThrow(BalanceUnavailableException::new);
    }

    public static class BalanceUnavailableException extends RuntimeException {
        public BalanceUnavailableException() {
            super("Unable to find balance");
        }
    }
}
