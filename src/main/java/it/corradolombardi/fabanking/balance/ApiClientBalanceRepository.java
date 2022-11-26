package it.corradolombardi.fabanking.balance;

import it.corradolombardi.fabanking.fabrikclient.BalancecFabrikResponse;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiException;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiStatusCodeException;
import it.corradolombardi.fabanking.fabrikclient.FabrikAccountCashClient;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.Balance;
import it.corradolombardi.fabanking.model.InformationUnavailableException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiClientBalanceRepository implements BalanceRepository {

    private final FabrikAccountCashClient fabrikAccountCashClient;

    public ApiClientBalanceRepository(FabrikAccountCashClient fabrikAccountCashClient) {
        this.fabrikAccountCashClient = fabrikAccountCashClient;
    }

    @Override
    public Balance balance(Long accountId) throws AccountNotFoundException, InformationUnavailableException {
        try {
            BalancecFabrikResponse response = fabrikAccountCashClient.balance(accountId);
            if (response.isOk()) {
                return response.getPayload().toBalance();
            }
            log.warn("Api client returned errors: {}", response.getErrors());

        } catch (FabrikApiStatusCodeException e) {
          if (e.userUnknown()) {
              throw new AccountNotFoundException(accountId);
          }
        } catch (FabrikApiException e) {
            log.error(e.getMessage(), e);
        }
        throw new InformationUnavailableException(accountId);
    }
}
