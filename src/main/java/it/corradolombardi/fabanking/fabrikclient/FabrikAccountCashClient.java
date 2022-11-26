package it.corradolombardi.fabanking.fabrikclient;

import java.util.function.Function;

import it.corradolombardi.fabanking.model.DateInterval;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * This class could be extracted to a module or a microservice responsible
 * for interaction with Fabrik. As possible evolution, it could be split in many classes
 * each responsible for a service (balance, transactions, transfer, etc.)
 */
@Slf4j
public class FabrikAccountCashClient {

    private static final String BALANCE_RESOURCE = "/balance";
    private static final String TRANSACTIONS_RESOURCE = "/transactions";
    private final RestTemplate restTemplate;

    private final String baseUrl;


    public FabrikAccountCashClient(String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    public BalancecFabrikResponse balance(Long accountId) throws FabrikApiException {

        return (BalancecFabrikResponse) callFabrik(accountId,
                                                   acc -> restTemplate.getForObject(
                                                       baseUrl + "/" + accountId + BALANCE_RESOURCE,
                                                       BalancecFabrikResponse.class));

    }

    public TransactionsFabrikResponse transactions(Long accountId, DateInterval dateInterval)
        throws FabrikApiException {
        Function<Long, BaseFabrikResponse> function =
            acc -> restTemplate.getForObject(baseUrl + "/" + acc + TRANSACTIONS_RESOURCE +
                                                        "?fromAccountingDate=" + dateInterval.dateFrom() +
                                                        "&toAccountingDate=" + dateInterval.dateTo(),
                                                    TransactionsFabrikResponse.class);
        return (TransactionsFabrikResponse) callFabrik(accountId, function);
    }

    private BaseFabrikResponse callFabrik(Long accountId,
                                          Function<Long, BaseFabrikResponse> function)
        throws FabrikApiException {
        try {
            return function.apply(accountId);
        } catch (HttpStatusCodeException e) {
            log.error("Error from fabrik API {} - {}", e.getStatusCode(),
                      e.getMessage());
            throw new FabrikApiStatusCodeException(e);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            throw new FabrikApiException(e);
        }
    }
}
