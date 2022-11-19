package it.corradolombardi.fabanking.rest;

import lombok.Data;

@Data
public class BalanceRest {
    private final String date;
    private final AmountRest availableBalance;
    private final AmountRest balance;
}
