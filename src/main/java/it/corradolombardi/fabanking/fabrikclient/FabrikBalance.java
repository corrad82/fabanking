package it.corradolombardi.fabanking.fabrikclient;

import com.google.gson.Gson;
import it.corradolombardi.fabanking.balance.Balance;
import it.corradolombardi.fabanking.model.Amount;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;

@Data
public class FabrikBalance {

    private final String date;
    private final String balance;
    private final String availableBalance;
    private final String currency;

    public static FabrikBalance fromJson(String jsonResponse) {
        return new Gson().fromJson(jsonResponse, FabrikBalance.class);
    }

    public static FabrikBalance nullValues() {
        return new FabrikBalance(null, null, null, null);
    }

    public Balance toBalance() {
        Currency currency = Currency.getInstance(this.currency);
        return new Balance(LocalDate.parse(date, DateTimeFormatter.ISO_DATE),
                new Amount(cents(availableBalance), currency),
                new Amount(cents(balance), currency));
    }

    private Long cents(String value) {
        return ((Double) (Double.parseDouble(value) * 100)).longValue();
    }
}
