package it.corradolombardi.fabanking.model;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException(Long accountId) {
        super(String.format("Account with id %s has not been found",
                            accountId));
    }
}
