package it.corradolombardi.fabanking.transactions;

import java.util.List;
import java.util.stream.Collectors;

import it.corradolombardi.fabanking.balance.InformationUnavailableException;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiException;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiStatusCodeException;
import it.corradolombardi.fabanking.fabrikclient.FabrikClient;
import it.corradolombardi.fabanking.fabrikclient.FabrikTransaction;
import it.corradolombardi.fabanking.fabrikclient.TransactionsFabrikResponse;
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
            TransactionsFabrikResponse response = fabrikClient.transactions(accountId, dateInterval);
            if (response.isOk()) {
                return response.getPayload().getList()
                               .stream().map(FabrikTransaction::toTransaction)
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
}
