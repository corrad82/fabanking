package it.corradolombardi.fabanking.rest;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TransferAmountsRest {
    private final AmountRest debtorAmount;
    private final AmountRest creditorAmount;
    private final LocalDate creditorCurrencyDate;
    private final Double exchangeRate;
}
