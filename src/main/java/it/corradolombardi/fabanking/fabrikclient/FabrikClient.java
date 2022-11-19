package it.corradolombardi.fabanking.fabrikclient;

import org.springframework.web.client.RestTemplate;

/**
 * This class could be extracted to a module or a microservice responsible
 * for interaction with Fabrik. As possible evolution, it could be split in many classes
 * each responsible for a service (balance, transactions, transfer, etc.)
 */
public class FabrikClient {

    private final RestTemplate restTemplate;


    public FabrikClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BalancecFabrikResponse balance(String accountId) {
        return restTemplate.getForObject("https://sandbox.platfr.io/" +
                "api/gbs/banking/v4.0/accounts/" + accountId +"/balance",
                BalancecFabrikResponse.class);
    }
}
