package it.corradolombardi.fabanking.fabrikclient;

import lombok.Data;

@Data
public class FabrikError {
    private static final String INVALID_ACCOUNT_CODE = "REQ004";
    private final String code;
    private final String description;
    private final String params;

    public boolean isInvalidAccount() {
        return INVALID_ACCOUNT_CODE.equals(code);
    }
}
