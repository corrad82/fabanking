package it.corradolombardi.fabanking.model;

import it.corradolombardi.fabanking.rest.MoneyTransferFeeRest;
import lombok.Data;

@Data
public class MoneyTransferFee {
    private final String code;
    private final String description;
    private final Amount amount;

    public MoneyTransferFeeRest toRest() {
        return new MoneyTransferFeeRest(code, description, amount);
    }
}
