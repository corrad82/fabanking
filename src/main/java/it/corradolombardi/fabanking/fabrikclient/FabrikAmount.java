package it.corradolombardi.fabanking.fabrikclient;

import lombok.Data;

@Data
public class FabrikAmount {
    private final Double debtorAmount;
    private final String debtorCurrency;
    private final Double creditorAmount;
    private final String creditorCurrency;
    private final String creditorCurrencyDate;
    private final Double exchangeRate;
}
