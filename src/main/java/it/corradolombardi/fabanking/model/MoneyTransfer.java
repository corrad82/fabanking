package it.corradolombardi.fabanking.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class MoneyTransfer {
    private final String moneyTransferId;
    private final String status;
    private final Direction direction;
    private final Person creditor;
    private final Person debtor;
    private final String cro;
    private final String uri;
    private final String trn;
    private final String description;
    private final LocalDateTime createdDatetime;
    private final LocalDateTime accountedDatetime;
    private final LocalDate debtorValueDate;
    private final LocalDate creditorValueDate;
    private final TransferAmounts amounts;
    private final boolean isUrgent;
    private final boolean isInstant;
    private final String feeType;
    private final String feeAccountId;
    private final List<MoneyTransferFee> fees;
    private final boolean hasTaxRelief;


    public enum Direction {
        INBOUND, OUTGOING
    }
}
