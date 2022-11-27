package it.corradolombardi.fabanking.fabrikclient;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FabrikMoneyTransferRequest {

    private final FabrikPerson creditor;
    private final String executionDate;
    private final String uri;
    private final String description;
    private final String amount;
    private final String currency;
    private final boolean isUrgent;
    private final boolean isInstant;
    private final String feeType;
    private final String feeAccountId;
    private final FabrikTaxRelief taxRelief;

}
