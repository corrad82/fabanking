package it.corradolombardi.fabanking.rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class MoneyTransferRest {
    private final String moneyTransferId;
    private final String status;
    private final String direction;
    private final PersonRest creditor;
    private final PersonRest debtor;
    private final String cro;
    private final String uri;
    private final String trn;
    private final String description;
    private final LocalDateTime createdDatetime;
    private final LocalDateTime accountedDatetime;
    private final LocalDate debtorValueDate;
    private final LocalDate creditorValueDate;
    private final TransferAmountsRest amounts;
    private final boolean isUrgent;
    private final boolean isInstant;
    private final String feeType;
    private final String feeAccountId;
    private final List<MoneyTransferFeeRest> fees;
    private final boolean hasTaxRelief;
}
