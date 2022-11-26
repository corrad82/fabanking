package it.corradolombardi.fabanking.model;

public class MoneyTransferException extends Exception {
    public MoneyTransferException(Long accountId, String message) {
        super("Error while transfering money from account " + accountId +  ": " + message);
    }
}
