package it.corradolombardi.fabanking.model;

import lombok.Data;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_DATE;

@Data
public class Balance {

    private final LocalDate date;
    private final Amount availableBalance;
    private final Amount balance;

    public BalanceRest toRest() {
        return new BalanceRest(date.format(ISO_DATE),
                               availableBalance.toRest(),
                               balance.toRest());
    }
}
