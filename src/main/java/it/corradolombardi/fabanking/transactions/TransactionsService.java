package it.corradolombardi.fabanking.transactions;


import it.corradolombardi.fabanking.model.InformationUnavailableException;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.DateInterval;
import it.corradolombardi.fabanking.model.Transaction;

import java.util.List;

public class TransactionsService {


    private final TransactionsRepository transactionsRepository;

    public TransactionsService(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    public List<Transaction> transactions(
        Long accountId, DateInterval dateInterval) throws AccountNotFoundException,
        InformationUnavailableException {

        tryToValidateParameters(accountId, dateInterval);

        return transactionsRepository.transactions(accountId, dateInterval);
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
