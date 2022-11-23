package it.corradolombardi.fabanking.transactions;


import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.DateInterval;
import it.corradolombardi.fabanking.model.Transaction;

import java.util.Collections;
import java.util.List;

public class TransactionsService {


    public List<Transaction> transactions(
            Long accountId, DateInterval dateInterval) throws AccountNotFoundException {

        try {
            tryToValidateParameters(accountId, dateInterval);
        } catch (IllegalArgumentException | AccountNotFoundException e) {
            throw e;
        }

        return Collections.emptyList();
    }

    private void tryToValidateParameters(Long accountId, DateInterval dateInterval) throws AccountNotFoundException {
        if(accountId < 0) {
            throw new AccountNotFoundException(accountId);
        }
        if (dateInterval.invalid()) {
            throw new IllegalArgumentException("Specified date interval is not valid: " + dateInterval);
        }
    }
}
