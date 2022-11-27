package it.corradolombardi.fabanking.fabrikclient;

import lombok.Data;

@Data
public class FabrikFee {
    private final String feeCode;
    private final String description;
    private final Double amount;
    private final String currency;
}
