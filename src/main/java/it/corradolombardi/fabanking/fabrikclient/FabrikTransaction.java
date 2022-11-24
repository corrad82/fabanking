package it.corradolombardi.fabanking.fabrikclient;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class FabrikTransaction {

    private final String transactionId;
    private final String operationId;
    private final String accountingDate;
    private final String valueDate;
    private final FabrikTransactionType type;
    private final String amount;
    private final String currency;
    private final String description;

    @Data
    public static class FabrikTransactionType {
    }
}
