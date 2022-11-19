package it.corradolombardi.fabanking.fabrikclient;

import lombok.Data;

import java.util.List;

@Data
public abstract class BaseFabrikResponse<T> {
    private final String status;
    private final List<FabrikError> errors;
    private final T payload;

    public boolean isOk() {
        return "OK".equals(status);
    }
}
