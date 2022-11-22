package it.corradolombardi.fabanking.balance;

public class InformationUnavailableException extends Exception {
    public InformationUnavailableException(Long accountId) {
        super("Unable to find information for account id: " + accountId);
    }
}
