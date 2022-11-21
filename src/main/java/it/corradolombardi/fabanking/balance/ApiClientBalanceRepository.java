package it.corradolombardi.fabanking.balance;

import it.corradolombardi.fabanking.fabrikclient.BalancecFabrikResponse;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiException;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiStatusCodeException;
import it.corradolombardi.fabanking.fabrikclient.FabrikClient;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ApiClientBalanceRepository implements BalanceRepository {

    private final FabrikClient fabrikClient;

    public ApiClientBalanceRepository(FabrikClient fabrikClient) {
        this.fabrikClient = fabrikClient;
    }

    @Override
    public Optional<Balance> balance(Long accountId) throws AccountNotFoundException, BalanceUnavailableException {
        try {
            BalancecFabrikResponse response = fabrikClient.balance(accountId);
            if (response.isOk()) {
                return Optional.of(response.getPayload().toBalance());
            }
            log.warn("Api client returned errors: {}", response.getErrors());

        } catch (FabrikApiStatusCodeException e) {
          if (e.userUnknown()) {
              throw new AccountNotFoundException(accountId);
          }
        } catch (FabrikApiException e) {
            log.error(e.getMessage(), e);
        }
        throw new BalanceUnavailableException();
    }
}
