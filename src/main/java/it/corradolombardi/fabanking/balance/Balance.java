package it.corradolombardi.fabanking.balance;

import java.time.LocalDate;

import it.corradolombardi.fabanking.model.Amount;
import lombok.Data;

@Data
public class Balance {

    private final LocalDate date;
    private final Amount availableBalance;
    private final Amount balance;
}
