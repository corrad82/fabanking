package it.corradolombardi.fabanking.rest;

import it.corradolombardi.fabanking.model.Balance;
import it.corradolombardi.fabanking.balance.BalanceService;
import it.corradolombardi.fabanking.balance.InformationUnavailableException;
import it.corradolombardi.fabanking.model.Amount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Currency;

import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BalanceController.class)
class BalanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void balanceReturned() throws Exception {

        mockMvc.perform(get("/balance/333"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
               .andExpect(jsonPath("$.date", is(now().format(ISO_DATE))))
               .andExpect(jsonPath("$.availableBalance.amount", is("100.00")))
               .andExpect(jsonPath("$.availableBalance.currency", is("EUR")))
               .andExpect(jsonPath("$.balance.amount", is("200.00")))
               .andExpect(jsonPath("$.balance.currency", is("EUR")));
    }

    @Test
    public void negativeBalanceReturned() throws Exception {

        mockMvc.perform(get("/balance/999"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
               .andExpect(jsonPath("$.availableBalance.amount", is("-100.00")))
               .andExpect(jsonPath("$.balance.amount", is("-200.00")));
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

    @TestConfiguration
    static class BalanceControllerTestConfiguration {

        // for test purposes, results are returned with positive balance if account id is 333
        // and with negative balance if account id is 999
        @Bean
        public BalanceService balanceService() {
            return new BalanceService(
                accountId -> {
                    if (accountId == 333L) {
                        return new Balance(
                            now(),
                            new Amount(10000L, Currency.getInstance("EUR")),
                            new Amount(20000L, Currency.getInstance("EUR"))
                        );
                    }
                    if (accountId == 999L) {
                        return new Balance(
                            now(),
                            new Amount(-10000L, Currency.getInstance("EUR")),
                            new Amount(-20000L, Currency.getInstance("EUR"))
                        );
                    }
                    throw new InformationUnavailableException(accountId);
                }
            );
        }
    }
}