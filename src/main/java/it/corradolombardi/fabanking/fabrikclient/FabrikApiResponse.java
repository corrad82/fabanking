package it.corradolombardi.fabanking.fabrikclient;

import lombok.Data;

import java.util.List;

@Data
public abstract class FabrikApiResponse<T> {
    private final String status;
    private final List<FabrikError> errors;
    private final T payload;

    public boolean isOk() {
        return "OK".equals(status);
    }

    public boolean isInvalidAccount() {
        return errors.stream()
            .anyMatch(FabrikError::isInvalidAccount);
    }

}
