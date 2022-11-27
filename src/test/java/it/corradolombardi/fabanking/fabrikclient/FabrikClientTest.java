package it.corradolombardi.fabanking.fabrikclient;


import it.corradolombardi.fabanking.fabrikclient.FabrikTransaction.FabrikTransactionType;
import it.corradolombardi.fabanking.model.DateInterval;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FabrikClientTest {

    @Mock
    private RestTemplate restTemplate;
    private FabrikClient fabrikClient;

    @BeforeEach
    void setUp() {
        fabrikClient = new FabrikClient("https://www.example.com/accounts", restTemplate);
    }

    @Test
    void balanceFound() throws FabrikApiException {

        expectApiResponse("accounts/123/balance",
                          "{" +
                              "    \"status\": \"OK\"," +
                              "    \"error\": []," +
                              "    \"payload\": {" +
                              "        \"date\": \"2022-11-19\"," +
                              "        \"balance\": 7.07," +
                              "        \"availableBalance\": 7.07," +
                              "        \"currency\": \"EUR\"" +
                              "    }" +
                              "}", BalancecFabrikApiResponse::fromJson);

        BalancecFabrikApiResponse response = fabrikClient.balance(123L);

        assertThat(response,
                   is(
                       new BalancecFabrikApiResponse(
                           "OK",
                           null,
                           new FabrikBalance("2022-11-19",
                                             "7.07",
                                             "7.07",
                                             "EUR"))));

    }

    @Test
    void responseError() throws FabrikApiException {
        expectApiResponse("accounts/789/balance",
                          "{" +
                              "    \"status\": \"KO\"," +
                              "    \"errors\": [" +
                              "        {" +
                              "            \"code\": \"REQ004\"," +
                              "            \"description\": \"Invalid account identifier\"," +
                              "            \"params\": \"\"" +
                              "        }" +
                              "    ]," +
                              "    \"payload\": {}" +
                              "}", BalancecFabrikApiResponse::fromJson);

        BalancecFabrikApiResponse response = fabrikClient.balance(789L);

        assertThat(response,
                   is(
                       new BalancecFabrikApiResponse(
                           "KO",
                           Collections.singletonList(
                               new FabrikError("REQ004",
                                               "Invalid account identifier",
                                               "")
                           ),
                           FabrikBalance.nullValues())));
    }

    @Test
    void exceptionDuringApiInteraction() {
        doThrow(new RestClientException("An error occurred"))
            .when(restTemplate)
            .getForObject(anyString(), any());
        assertThrows(FabrikApiException.class, () -> fabrikClient.balance(4343L));

    }

    @Test
    void transactionsFound() throws FabrikApiException {

        expectApiResponse("transactions?fromAccountingDate=2022-11-24&toAccountingDate=2022-11-25",
                          "{" +
                              "    \"status\": \"OK\"," +
                              "    \"error\": []," +
                              "    \"payload\": {" +
                              "        \"list\": [" +
                              "            {" +
                              "                \"transactionId\": \"280528395800\"," +
                              "                \"operationId\": \"22000251592384\"," +
                              "                \"accountingDate\": \"2022-11-25\"," +
                              "                \"valueDate\": \"2022-11-25\"," +
                              "                \"type\": {" +
                              "                    \"enumeration\": \"GBS_TRANSACTION_TYPE\"," +
                              "                    \"value\": \"GBS_ACCOUNT_TRANSACTION_TYPE_0010\"" +
                              "                }," +
                              "                \"amount\": 5.82," +
                              "                \"currency\": \"EUR\"," +
                              "                \"description\": \"BD PROVAB INTERNET       DA 03268.22300         " +
                              "Data Ordine 25112022 REMITINFO\"" +
                              "            }," +
                              "            {" +
                              "                \"transactionId\": \"280528394600\"," +
                              "                \"operationId\": \"22000251592375\"," +
                              "                \"accountingDate\": \"2022-11-25\"," +
                              "                \"valueDate\": \"2022-11-25\"," +
                              "                \"type\": {" +
                              "                    \"enumeration\": \"GBS_TRANSACTION_TYPE\"," +
                              "                    \"value\": \"GBS_ACCOUNT_TRANSACTION_TYPE_0010\"" +
                              "                }," +
                              "                \"amount\": 10.94," +
                              "                \"currency\": \"EUR\"," +
                              "                \"description\": \"BD PROVAB INTERNET       DA 03268.22300         " +
                              "Data Ordine 25112022 REMITINFO\"" +
                              "            }" +
                              "            ]" +
                              "    }" +
                              "}", TransactionsFabrikApiResponse::fromJson);

        TransactionsFabrikApiResponse response = fabrikClient.transactions(123L,
                                                                           DateInterval.of("2022-11-23", "2022-11-24"));

        assertThat(response,
                   is(
                       new TransactionsFabrikApiResponse(
                           "OK",
                           null,
                           new FabrikTransactionsList(
                               List.of(
                                   new FabrikTransaction("280528395800", "22000251592384",
                                                         "2022-11-25", "2022-11-25",
                                                         new FabrikTransactionType("GBS_TRANSACTION_TYPE",
                                                                                   "GBS_ACCOUNT_TRANSACTION_TYPE_0010"),
                                                         "5.82",
                                                         "EUR",
                                                         "BD PROVAB INTERNET       DA 03268.22300         Data Ordine" +
                                                             " 25112022 REMITINFO"),
                                   new FabrikTransaction("280528394600",
                                                         "22000251592375",
                                                         "2022-11-25", "2022-11-25",
                                                         new FabrikTransactionType("GBS_TRANSACTION_TYPE",
                                                                                   "GBS_ACCOUNT_TRANSACTION_TYPE_0010"),
                                                         "10.94",
                                                         "EUR",
                                                         "BD PROVAB INTERNET       DA 03268.22300         Data Ordine 25112022 REMITINFO"))))));

    }

    @Test
    void responseErrorDuringTransactionsLookup() throws FabrikApiException {
        expectApiResponse("accounts/789/transactions?fromAccountingDate=2022-11-10&toAccountingDate=2022-11-24",
                          "{" +
                              "    \"status\": \"KO\"," +
                              "    \"errors\": [" +
                              "        {" +
                              "            \"code\": \"REQ017\"," +
                              "            \"description\": \"Invalid date format\"," +
                              "            \"params\": \"\"" +
                              "        }" +
                              "    ]," +
                              "    \"payload\": {}" +
                              "}", TransactionsFabrikApiResponse::fromJson);

        TransactionsFabrikApiResponse response = fabrikClient.transactions(789L,
                                                                           DateInterval.of("2022-11-10", "2022-11-24"));

        assertThat(response,
                   is(
                       new TransactionsFabrikApiResponse(
                           "KO",
                           Collections.singletonList(
                               new FabrikError("REQ017",
                                               "Invalid date format",
                                               "")
                           ),
                           FabrikTransactionsList.nullList())));
    }

    @Test
    void exceptionDuringApiTransactionsLookup() {
        doThrow(new RestClientException("An error occurred"))
            .when(restTemplate)
            .getForObject(anyString(), any());
        assertThrows(FabrikApiException.class, () -> fabrikClient.transactions(4343L,
                                                                               DateInterval.of("2022-11-01",
                                                                                               "2022-11-10")));

    }

    //TODO: missing tests for money transfer, as a real response to be used to simulate interaction is not available so far.

    private void expectApiResponse(String request, String jsonResponse,
                                   Function<String, FabrikApiResponse> jsonParser) {
        when(restTemplate
                 .getForObject(
                     anyString(),
                     any()))
            .thenReturn(jsonParser.apply(jsonResponse));
    }


}