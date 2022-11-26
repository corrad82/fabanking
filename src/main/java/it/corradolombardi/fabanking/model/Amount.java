package it.corradolombardi.fabanking.model;

import it.corradolombardi.fabanking.rest.AmountRest;
import lombok.Data;

import java.util.Currency;

import static java.lang.String.format;
import static java.util.Locale.US;

@Data
public class Amount {
    private final Long cents;
    private final Currency currency;

    public AmountRest toRest() {
        double d = (double) cents / 100;
        String amount = format(US, "%.2f", d);
        return new AmountRest(amount, currency.getCurrencyCode());
    }
}
