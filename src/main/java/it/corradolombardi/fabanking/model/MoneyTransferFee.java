package it.corradolombardi.fabanking.model;

import lombok.Data;

@Data
public class MoneyTransferFee {
    private final String code;
    private final String description;
    private final Amount amount;
}
