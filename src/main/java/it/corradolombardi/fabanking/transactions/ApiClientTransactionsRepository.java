package it.corradolombardi.fabanking.transactions;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import it.corradolombardi.fabanking.fabrikclient.FabrikTransaction.FabrikTransactionType;
import it.corradolombardi.fabanking.model.Amount;
import it.corradolombardi.fabanking.model.InformationUnavailableException;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiException;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiStatusCodeException;
import it.corradolombardi.fabanking.fabrikclient.FabrikClient;
import it.corradolombardi.fabanking.fabrikclient.FabrikTransaction;
import it.corradolombardi.fabanking.fabrikclient.TransactionsFabrikApiResponse;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.DateInterval;
import it.corradolombardi.fabanking.model.Transaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiClientTransactionsRepository
    implements TransactionsRepository {

    private final FabrikClient fabrikClient;

    public ApiClientTransactionsRepository(FabrikClient fabrikClient) {
        this.fabrikClient = fabrikClient;
    }

    @Override
    public List<Transaction> transactions(Long accountId, DateInterval dateInterval)
        throws AccountNotFoundException, InformationUnavailableException {

        // FIXME: this block is similar to the one in ApiClientBalanceRepository. Shall we use a common method?
        try {
            TransactionsFabrikApiResponse response = fabrikClient.transactions(accountId, dateInterval);
            if (response.isOk()) {
                return response.getPayload().getList()
                               .stream().map(this::toTransaction)
                               .collect(Collectors.toList());
            }
            log.warn("Api client returned errors: {}", response.getErrors());

        } catch (FabrikApiStatusCodeException e) {
            if (e.userUnknown()) {
                throw new AccountNotFoundException(accountId);
            }
        } catch (FabrikApiException e) {
            log.error(e.getMessage(), e);
        }
        throw new InformationUnavailableException(accountId, dateInterval);
    }

    public Transaction toTransaction(FabrikTransaction transaction) {
        FabrikTransactionType type = transaction.getType();
        return Transaction
            .builder()
            .transactionId(transaction.getTransactionId())
            .operationId(transaction.getOperationId())
            .accountingDate(LocalDate.parse(transaction.getAccountingDate()))
            .valueDate(LocalDate.parse(transaction.getValueDate()))
            .transactionType(new Transaction.TransactionType(type.getEnumeration(), type.getValue()))
            .amount(new Amount(cents(transaction.getAmount()), Currency.getInstance(transaction.getCurrency())))
            .description(transaction.getDescription())
            .build();
    }

    private Long cents(String value) {
        return ((Double) (Double.parseDouble(value) * 100)).longValue();
    }

}
