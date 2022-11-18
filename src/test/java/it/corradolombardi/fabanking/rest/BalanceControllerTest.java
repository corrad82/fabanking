package it.corradolombardi.fabanking.rest;

import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Currency;
import java.util.Optional;

import it.corradolombardi.fabanking.balance.Balance;
import it.corradolombardi.fabanking.balance.BalanceConfiguration;
import it.corradolombardi.fabanking.balance.BalanceService;
import it.corradolombardi.fabanking.model.Amount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BalanceControllerTest {

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(
        new BalanceController(
            new BalanceService(
                accountId -> {
                    // for testing purpose, only account with id 333 has a balance
                    if (accountId == 333L) {
                        return Optional.of(
                            new Balance(
                                now(),
                                new Amount(10000L, Currency.getInstance("EUR")),
                                new Amount(20000L, Currency.getInstance("EUR"))
                            )
                        );
                    }
                    return Optional.empty();
                }
            )
        )).build();


    @Test
    public void balanceReturned() throws Exception {

        mockMvc.perform(get("/balance/333"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
               .andExpect(jsonPath("$.date", is(now().format(ISO_DATE))))
               .andExpect(jsonPath("$.availableBalance.cents", is(10000)))
               .andExpect(jsonPath("$.availableBalance.currency", is("EUR")))
               .andExpect(jsonPath("$.balance.currency", is("EUR")))
               .andExpect(jsonPath("$.balance.currency", is("EUR")));
    }

    @Test
    public void balanceNotReturnedByExternalService() throws Exception {
        mockMvc.perform(get("/balance/111"))
               .andExpect(status().isInternalServerError());
    }

    @Test
    public void accountNotFound() throws Exception {
        mockMvc.perform(get("/balance/-111123"))
               .andExpect(status().isNotFound());
    }

    @Test
    public void invalidAccountId() throws Exception {
        mockMvc.perform(get("/balance/asdfasdf"))
               .andExpect(status().isBadRequest());
    }

    @Test
    public void postNotAllowed() throws Exception {
        mockMvc.perform(post("/balance/222"))
               .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void deleteNotAllowed() throws Exception {
        mockMvc.perform(delete("/balance/123"))
               .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void patchNotAllowed() throws Exception {
        mockMvc.perform(patch("/balance/222"))
               .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void putNotAllowed() throws Exception {
        mockMvc.perform(put("/balance/222"))
               .andExpect(status().isMethodNotAllowed());
    }

//    @TestConfiguration
//    static class BalanceControllerTestConfiguration {
//
//        // for test purposes, results are returned iif account id is 333
//        @Bean
//        public BalanceService balanceService() {
//            return new BalanceService(
//                accountId -> {
//                    if (accountId == 333L) {
//                        return Optional.of(
//                            new Balance(
//                                now(),
//                                new Amount(10000L, Currency.getInstance("EUR")),
//                                new Amount(20000L, Currency.getInstance("EUR"))
//                            )
//                        );
//                    }
//                    return Optional.empty();
//                }
//            );
//        }
//
//        @Bean
//        public MockMvc mockMvc(BalanceService balanceService) {
//            return MockMvcBuilders.standaloneSetup(
//                new BalanceController(balanceService)
//            ).build();
//        }
//    }
}