package it.corradolombardi.fabanking.model;

import java.util.Objects;
import java.util.stream.Stream;

import lombok.Data;

@Data
public class MoneyTransferRequest {
    private final Long accountId;
    private final String receiverName;
    private final String description;
    private final MoneyTransferRequest.Amount amount;
    private final String executionDate;

    public boolean isInvalid() {
        return Stream.of(accountId,
                         receiverName,
                         description,
                         amount,
                         executionDate)
            .anyMatch(Objects::isNull);
    }

    @Data
    public static class Amount {
        private final Double amount;
        private final String currency;
    }
}
