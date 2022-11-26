package it.corradolombardi.fabanking.rest;

import it.corradolombardi.fabanking.model.Amount;
import lombok.Data;

@Data
public class MoneyTransferFeeRest {
    private final String code;
    private final String description;
    private final Amount amount;
}
