package it.corradolombardi.fabanking.fabrikclient;

import java.util.List;

import lombok.Data;

@Data
public class FabrikMoneyTransfer {

    private final String moneyTransferId;
    private final String status;
    private final String direction;
    private final FabrikPerson creditor;
    private final FabrikPerson debtor;
    private final String cro;
    private final String trn;
    private final String uri;
    private final String description;
    private final String createdDatetime;
    private final String accountedDatetime;
    private final String debtorValueDate;
    private final String creditorValueDate;
    private final FabrikAmount amount;
    private final boolean isUrgent;
    private final boolean isInstant;
    private final String feeType;
    private final String feeAccountID;
    private final List<FabrikFee> fees;
    private final boolean hasTaxRelief;


}
