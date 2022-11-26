package it.corradolombardi.fabanking.rest;

import lombok.Data;

@Data
public class AmountRest {

    private final String amount;
    private final String currency;
}
