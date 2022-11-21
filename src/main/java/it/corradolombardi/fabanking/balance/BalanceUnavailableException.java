package it.corradolombardi.fabanking.balance;

public class BalanceUnavailableException extends Exception {
    public BalanceUnavailableException() {
        super("Unable to find balance");
    }
}
