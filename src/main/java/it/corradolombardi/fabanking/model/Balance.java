package it.corradolombardi.fabanking.model;

import java.time.LocalDate;

import it.corradolombardi.fabanking.rest.BalanceRest;
import lombok.Data;

@Data
public class Balance {

    private final LocalDate date;
    private final Amount availableBalance;
    private final Amount balance;

    public BalanceRest toRest() {
        return new BalanceRest(date,
                               availableBalance.toRest(),
                               balance.toRest());
    }
}
