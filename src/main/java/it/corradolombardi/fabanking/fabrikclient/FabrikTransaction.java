package it.corradolombardi.fabanking.fabrikclient;

import java.time.LocalDate;
import java.util.Currency;

import it.corradolombardi.fabanking.model.Amount;
import it.corradolombardi.fabanking.model.Transaction;
import lombok.Data;

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

    public Transaction toTransaction() {
        return Transaction
            .builder()
            .transactionId(transactionId)
            .operationId(operationId)
            .accountingDate(LocalDate.parse(accountingDate))
            .valueDate(LocalDate.parse(valueDate))
            .transactionType(new Transaction.TransactionType(type.getEnumeration(), type.getValue()))
            .amount(new Amount(cents(amount), Currency.getInstance(currency)))
            .description(description)
            .build();
    }

    private Long cents(String value) {
        return ((Double) (Double.parseDouble(value) * 100)).longValue();
    }

    @Data
    public static class FabrikTransactionType {
        private final String enumeration;
        private final String value;
    }
}
