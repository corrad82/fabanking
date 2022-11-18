package it.corradolombardi.fabanking.model;

import java.util.Currency;

import lombok.Data;

@Data
public class Amount {
    private final Long cents;
    private final Currency currency;
}
