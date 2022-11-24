package it.corradolombardi.fabanking.balance;

import it.corradolombardi.fabanking.model.DateInterval;

public class InformationUnavailableException extends Exception {
    public InformationUnavailableException(Long accountId) {
        super(String.format("Unable to find information for account id: %d", accountId));
    }

    public InformationUnavailableException(Long accountId, DateInterval dateInterval) {
        super(
            String.format("Unable to find information for account id: %d in date interval: %s", accountId,
                          dateInterval));
    }
}
