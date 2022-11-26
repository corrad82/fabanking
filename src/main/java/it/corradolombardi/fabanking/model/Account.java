package it.corradolombardi.fabanking.model;

import it.corradolombardi.fabanking.rest.AccountRest;
import lombok.Data;

@Data
public class Account {
    private final String accountCode;
    private final String bicCode;

    public AccountRest toRest() {
        return new AccountRest(accountCode, bicCode);
    }
}
