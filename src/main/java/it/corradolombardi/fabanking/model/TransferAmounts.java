package it.corradolombardi.fabanking.model;

import java.time.LocalDate;

import it.corradolombardi.fabanking.rest.TransferAmountsRest;
import lombok.Data;

@Data
public class TransferAmounts {
    private final Amount debtorAmount;
    private final Amount creditorAmount;
    private final LocalDate creditorCurrencyDate;
    private final Double exchangeRate;

    public TransferAmountsRest toRest() {
        return new TransferAmountsRest(debtorAmount.toRest(), creditorAmount.toRest(),
                                       creditorCurrencyDate, exchangeRate);
    }
}
