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
        String amount = amountToString();
        return new AmountRest(amount, currency.getCurrencyCode());
    }

    public String amountToString() {
        double amountAsDouble = (double) cents / 100;
        return format(US, "%.2f", amountAsDouble);
    }
}
