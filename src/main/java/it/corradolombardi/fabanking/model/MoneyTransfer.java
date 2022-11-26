package it.corradolombardi.fabanking.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import it.corradolombardi.fabanking.rest.MoneyTransferRest;
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

    public MoneyTransferRest toRest() {
        return new MoneyTransferRest(moneyTransferId,
                                     status,
                                     direction.name(),
                                     creditor.toRest(),
                                     debtor.toRest(),
                                     cro,
                                     uri,
                                     trn,
                                     description,
                                     createdDatetime,
                                     accountedDatetime,
                                     debtorValueDate,
                                     creditorValueDate,
                                     amounts.toRest(),
                                     isUrgent,
                                     isInstant,
                                     feeType,
                                     feeAccountId,
                                     fees.stream().map(MoneyTransferFee::toRest).collect(Collectors.toList()),
                                     hasTaxRelief);
    }


    public enum Direction {
        INBOUND, OUTGOING
    }
}
