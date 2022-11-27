package it.corradolombardi.fabanking.balance;

import java.time.LocalDate;
import java.util.Currency;

import it.corradolombardi.fabanking.fabrikclient.BalancecFabrikApiResponse;
import it.corradolombardi.fabanking.fabrikclient.FabrikClient;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiException;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiStatusCodeException;
import it.corradolombardi.fabanking.fabrikclient.FabrikBalance;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.Amount;
import it.corradolombardi.fabanking.model.Balance;
import it.corradolombardi.fabanking.model.InformationUnavailableException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiClientBalanceRepository implements BalanceRepository {

    private final FabrikClient fabrikClient;

    public ApiClientBalanceRepository(FabrikClient fabrikClient) {
        this.fabrikClient = fabrikClient;
    }

    @Override
    public Balance balance(Long accountId) throws AccountNotFoundException, InformationUnavailableException {
        try {
            BalancecFabrikApiResponse response = fabrikClient.balance(accountId);
            if (response.isOk()) {
                return toBalance(response.getPayload());
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

    private Balance toBalance(FabrikBalance payload) {
        Currency currency = Currency.getInstance(payload.getCurrency());
        return new Balance(LocalDate.parse(payload.getDate()),
                           new Amount(cents(payload.getAvailableBalance()), currency),
                           new Amount(cents(payload.getBalance()), currency));
    }
    private Long cents(String value) {
        return ((Double) (Double.parseDouble(value) * 100)).longValue();
    }

}
