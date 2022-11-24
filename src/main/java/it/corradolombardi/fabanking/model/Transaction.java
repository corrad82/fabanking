package it.corradolombardi.fabanking.model;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Transaction {

    private final String transactionId;
    private final String operationId;
    private final LocalDate accountingDate;
    private final LocalDate valueDate;
    private final TransactionType transactionType;
    private final Amount amount;
    private final String description;

    @Data
    public static  class TransactionType {
        private final String enumeration;
        private final String value;
    }
}
