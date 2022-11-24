package it.corradolombardi.fabanking.rest;

import it.corradolombardi.fabanking.model.AmountRest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TransactionRest {

    private final String transactionId;
    private final String operationId;
    private final String accountingDate;
    private final String valueDate;
    private final TransactionTypeRest type;
    private final AmountRest amount;
    private final String description;

    @Data
    public static class TransactionTypeRest {
        private final String enumeration;
        private final String description;
    }
}
