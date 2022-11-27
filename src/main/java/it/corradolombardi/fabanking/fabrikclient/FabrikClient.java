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
public class FabrikClient {

    private static final String BALANCE_RESOURCE = "/balance";
    private static final String TRANSACTIONS_RESOURCE = "/transactions";
    private static final String MONEY_TRANSFER_RESOURCE = "/payments/money-transfers";
    private final RestTemplate restTemplate;

    private final String baseUrl;


    public FabrikClient(String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    public BalancecFabrikApiResponse balance(Long accountId) throws FabrikApiException {

        return (BalancecFabrikApiResponse) callFabrik(accountId,
                                                   acc -> restTemplate.getForObject(
                                                       baseUrl + "/" + accountId + BALANCE_RESOURCE,
                                                       BalancecFabrikApiResponse.class));

    }

    public TransactionsFabrikApiResponse transactions(Long accountId, DateInterval dateInterval)
        throws FabrikApiException {
        Function<Long, FabrikApiResponse> restCallFunction =
            acc -> restTemplate.getForObject(baseUrl + "/" + acc + TRANSACTIONS_RESOURCE +
                                                        "?fromAccountingDate=" + dateInterval.dateFrom() +
                                                        "&toAccountingDate=" + dateInterval.dateTo(),
                                                    TransactionsFabrikApiResponse.class);
        return (TransactionsFabrikApiResponse) callFabrik(accountId, restCallFunction);
    }

    public MoneyTransferFabrikApiResponse moneyTransfer(Long accountId, FabrikMoneyTransferRequest request)
        throws FabrikApiException {

        String url = baseUrl + "/" + accountId + MONEY_TRANSFER_RESOURCE;
        Function<Long, FabrikApiResponse> fn =
            acc -> restTemplate.postForObject(url, request, MoneyTransferFabrikApiResponse.class);

        return (MoneyTransferFabrikApiResponse) callFabrik(accountId, fn);
    }

    private FabrikApiResponse callFabrik(Long accountId,
                                         Function<Long, FabrikApiResponse> restCallFunction)
        throws FabrikApiException {
        try {
            return restCallFunction.apply(accountId);
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
