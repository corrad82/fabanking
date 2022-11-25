package it.corradolombardi.fabanking.transactions;

import java.util.List;

import it.corradolombardi.fabanking.balance.InformationUnavailableException;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiException;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.DateInterval;
import it.corradolombardi.fabanking.model.Transaction;

public interface TransactionsRepository {
    List<Transaction> transactions(Long accountId, DateInterval dateInterval) throws AccountNotFoundException,
        InformationUnavailableException, FabrikApiException;
}
