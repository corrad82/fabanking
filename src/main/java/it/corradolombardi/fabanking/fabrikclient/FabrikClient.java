package it.corradolombardi.fabanking.fabrikclient;

import lombok.extern.slf4j.Slf4j;
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
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            throw new FabrikApiException(e);
        }
    }
}
