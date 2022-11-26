package it.corradolombardi.fabanking.moneytransfer;

import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.MoneyTransfer;
import it.corradolombardi.fabanking.model.MoneyTransferException;
import it.corradolombardi.fabanking.model.MoneyTransferRequest;

public interface MoneyTransferRepository {
    MoneyTransfer transfer(MoneyTransferRequest request) throws AccountNotFoundException, MoneyTransferException;
}
