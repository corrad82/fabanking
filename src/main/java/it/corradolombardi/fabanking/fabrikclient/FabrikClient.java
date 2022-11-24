package it.corradolombardi.fabanking.fabrikclient;

import java.util.Map;

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
public class FabrikClient {

    private static final String BALANCE_RESOURCE = "/balance";
    private final RestTemplate restTemplate;

    private final String baseUrl;


    public FabrikClient(String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    public BalancecFabrikResponse balance(Long accountId) throws FabrikApiException {
        try {
            return restTemplate.getForObject(baseUrl + "/" + accountId + BALANCE_RESOURCE,
                    BalancecFabrikResponse.class);
        } catch (HttpStatusCodeException e) {
            log.error("Error from fabrik API {} - {}", e.getStatusCode(),
                    e.getMessage());
            throw new FabrikApiStatusCodeException(e);
        }
        catch (RestClientException e) {
            log.error(e.getMessage(), e);
            throw new FabrikApiException(e);
        }
    }

    public TransactionsFabrikResponse transactions(Long accountId, DateInterval dateInterval)
        throws FabrikApiException {
        try {
            Map<String, String> params = Map.of("fromAccountingDate", dateInterval.dateFrom(),
                                                "toAccountingDate", dateInterval.dateTo());
            return restTemplate.getForObject(baseUrl + "/" + accountId + "transactions",
                                             TransactionsFabrikResponse.class,
                                             params);
        } catch (HttpStatusCodeException e) {
            log.error("Error from fabrik API {} - {}", e.getStatusCode(),
                      e.getMessage());
            throw new FabrikApiStatusCodeException(e);
        }
        catch (RestClientException e) {
            log.error(e.getMessage(), e);
            throw new FabrikApiException(e);
        }
    }
}
