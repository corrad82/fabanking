package it.corradolombardi.fabanking.rest;

import java.time.LocalDate;

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
    private final LocalDate accountingDate;
    private final LocalDate valueDate;
    private final TransactionTypeRest type;
    private final AmountRest amount;
    private final String description;

    @Data
    public static class TransactionTypeRest {
        private final String enumeration;
        private final String description;
    }
}
