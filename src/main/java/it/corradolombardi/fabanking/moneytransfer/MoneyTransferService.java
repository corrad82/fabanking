package it.corradolombardi.fabanking.moneytransfer;

import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.MoneyTransfer;
import it.corradolombardi.fabanking.model.MoneyTransferException;
import it.corradolombardi.fabanking.model.MoneyTransferRequest;

public class MoneyTransferService {

    private final MoneyTransferRepository moneyTransferRepository;

    public MoneyTransferService(MoneyTransferRepository moneyTransferRepository) {
        this.moneyTransferRepository = moneyTransferRepository;
    }

    public MoneyTransfer transfer(MoneyTransferRequest request) throws AccountNotFoundException,
        MoneyTransferException {
        validate(request);
        return moneyTransferRepository.transfer(request);

    }

    private void validate(MoneyTransferRequest request) throws AccountNotFoundException {
        // request invalid throws illegalArgumentException
        if (request.isInvalid()) {
            throw new IllegalArgumentException("All request parameters: accountId, receiverName, " +
                                                   "description, amount, currency, execution date must be set");
        }
        if (request.getAccountId() < 0) {
            throw new AccountNotFoundException(request.getAccountId());
        }
    }
}
