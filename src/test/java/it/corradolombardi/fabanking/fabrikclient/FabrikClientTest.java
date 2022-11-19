package it.corradolombardi.fabanking.fabrikclient;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FabrikClientTest {

    @Mock
    private RestTemplate restTemplate;
    private FabrikClient fabrikClient;

    @BeforeEach
    void setUp() {
        fabrikClient = new FabrikClient(restTemplate);
    }

    @Test
    void balanceFound() {

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
                "}");

        BalancecFabrikResponse response = fabrikClient.balance("123");

        assertThat(response,
                is(
                        new BalancecFabrikResponse(
                                "OK",
                                null,
                                new FabrikBalance("2022-11-19",
                                        "7.07",
                                        "7.07",
                                        "EUR"))));

    }

    @Test
    void responseError() {
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
                        "}");

        BalancecFabrikResponse response = fabrikClient.balance("789");

        assertThat(response,
                is(
                        new BalancecFabrikResponse(
                                "KO",
                                Collections.singletonList(
                                        new FabrikError("REQ004",
                                                "Invalid account identifier",
                                                "")
                                ),
                                FabrikBalance.nullValues())));
    }

    @Test
    void timeout() {
        when(restTemplate.getForObject(any(), eq(BalancecFabrikResponse.class)))
                .then
    }

    private void expectApiResponse(String request, String jsonResponse) {
        when(restTemplate
                .getForObject(
                        ArgumentMatchers.contains(request),
                        eq(BalancecFabrikResponse.class)))
                .thenReturn(BalancecFabrikResponse.fromJson(jsonResponse));
    }



    // timeout

    // remote server error


}