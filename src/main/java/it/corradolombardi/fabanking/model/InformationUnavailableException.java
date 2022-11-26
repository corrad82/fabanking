package it.corradolombardi.fabanking.model;

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
