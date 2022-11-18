package it.corradolombardi.fabanking.model;

import static java.lang.String.format;

import java.util.Currency;

import it.corradolombardi.fabanking.rest.AmountRest;
import lombok.Data;

@Data
public class Amount {
    private final Long cents;
    private final Currency currency;

    public AmountRest toRest() {
        Double d = (double) (cents / 100);
        return new AmountRest(format("%.2f", d), currency.getCurrencyCode());
    }
}
