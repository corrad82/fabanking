package it.corradolombardi.fabanking.transactions;

import java.util.List;

import it.corradolombardi.fabanking.model.InformationUnavailableException;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.DateInterval;
import it.corradolombardi.fabanking.model.Transaction;

public interface TransactionsRepository {
    List<Transaction> transactions(Long accountId, DateInterval dateInterval) throws AccountNotFoundException,
        InformationUnavailableException;
}
