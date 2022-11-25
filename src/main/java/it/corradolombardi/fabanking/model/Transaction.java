package it.corradolombardi.fabanking.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import it.corradolombardi.fabanking.rest.TransactionRest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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

    public TransactionRest toRest() {
        return TransactionRest
            .builder()
            .transactionId(transactionId)
            .operationId(operationId)
            .accountingDate(accountingDate.format(DateTimeFormatter.ISO_DATE))
            .valueDate(valueDate.format(DateTimeFormatter.ISO_DATE))
            .type(transactionType.toRest())
            .amount(amount.toRest())
            .description(description)
            .build();
    }

    @ToString
    @EqualsAndHashCode
    public static  class TransactionType {
        private final String enumeration;
        private final String value;

        public TransactionType(String enumeration, String value) {
            this.enumeration = enumeration;
            this.value = value;
        }

        public TransactionRest.TransactionTypeRest toRest() {
            return new TransactionRest.TransactionTypeRest(enumeration, value);
        }
    }
}
