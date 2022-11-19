package it.corradolombardi.fabanking.balance;

import it.corradolombardi.fabanking.fabrikclient.BalancecFabrikResponse;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiException;
import it.corradolombardi.fabanking.fabrikclient.FabrikClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ApiClientBalanceRepository implements BalanceRepository {

    private final FabrikClient fabrikClient;

    public ApiClientBalanceRepository(FabrikClient fabrikClient) {
        this.fabrikClient = fabrikClient;
    }

    @Override
    public Optional<Balance> balance(Long accountId) {
        try {
            BalancecFabrikResponse response = fabrikClient.balance(accountId);
            if (response.isOk()) {
                return Optional.of(response.getPayload().toBalance());
            }
            log.warn("Api client returned errors: {}", response.getErrors());

        } catch (FabrikApiException e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }
}
