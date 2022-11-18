package it.corradolombardi.fabanking.rest;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BalanceRest {
    private final LocalDate date;
    private final AmountRest availableBalance;
    private final AmountRest balance;
}
