package it.corradolombardi.fabanking.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import it.corradolombardi.fabanking.balance.InformationUnavailableException;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.Amount;
import it.corradolombardi.fabanking.model.DateInterval;
import it.corradolombardi.fabanking.model.Transaction;
import it.corradolombardi.fabanking.transactions.TransactionsRepository;
import it.corradolombardi.fabanking.transactions.TransactionsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransactionsController.class)
class TransactionsControllerTest {

    private static final long ACCOUNT_NOT_FOUND_ID = 55555L;
    public static final long ACCOUNT_WITH_TRANSACTIONS_ID = 260806L;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void negativeOrNotFoundAccountId() throws Exception {
        mockMvc.perform(get("/transactions/-123456?fromAccountingDate=2022-11-20&toAccountingDate=2022-11-24"))
               .andExpect(status().isNotFound());

        mockMvc.perform(
                   get("/transactions/" + ACCOUNT_NOT_FOUND_ID + "?fromAccountingDate=2022-11-20&toAccountingDate" +
                           "=2022-11-24"))
               .andExpect(status().isNotFound());
    }

    @Test
    void missingOrInvalidDatesReturnBadRequest() throws Exception {
        mockMvc.perform(get("/transactions/123456"))
               .andExpect(status().isBadRequest());

        mockMvc.perform(get("/transactions/123456?fromAccountingDate=2022-11-20"))
               .andExpect(status().isBadRequest());

        mockMvc.perform(get("/transactions/123456?toAccountingDate=2022-11-20"))
               .andExpect(status().isBadRequest());

        mockMvc.perform(get("/transactions/123456?toAccountingDate=foo&toAccountingDate=bar"))
               .andExpect(status().isBadRequest());
    }

    @Test
    void malformedAccountIdReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/transactions/asdf?fromAccountingDate=2022-11-20&toAccountingDate=2022-11-24"))
               .andExpect(status().isBadRequest());
    }

    @Test
    void methodsNotAllowed() throws Exception {

        mockMvc.perform(patch("/transactions/123456?fromAccountingDate=2022-11-20&toAccountingDate=2022-11-24"))
               .andExpect(status().isMethodNotAllowed());

        mockMvc.perform(put("/transactions/123456?fromAccountingDate=2022-11-20&toAccountingDate=2022-11-24"))
               .andExpect(status().isMethodNotAllowed());

        mockMvc.perform(delete("/transactions/123456?fromAccountingDate=2022-11-20&toAccountingDate=2022-11-24"))
               .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void informationNotAvailable() throws Exception {
        mockMvc.perform(get("/transactions/123456?fromAccountingDate=2022-11-20&toAccountingDate=2022-11-24"))
               .andExpect(status().isInternalServerError())
               .andExpect(jsonPath("$.error", is(
                   "Unable to find information for account id: 123456 in date interval: DateInterval(from=2022-11-20," +
                       " to=2022-11-24)")));
    }

    @Test
    void transactionsReturned() throws Exception {
        mockMvc.perform(get("/transactions/" + ACCOUNT_WITH_TRANSACTIONS_ID +
                                "  ?fromAccountingDate=2022-11-20&toAccountingDate=2022-11-24"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.*").isArray())
               .andExpect(jsonPath("$.*", hasSize(2)))
               .andExpect(jsonPath("$[0].transactionId", is("t1")))
               .andExpect(jsonPath("$[0].operationId", is("op1")))
               .andExpect(jsonPath("$[0].accountingDate", is("2022-11-21")))
               .andExpect(jsonPath("$[0].valueDate", is("2022-11-23")))
               .andExpect(jsonPath("$[0].type.enumeration", is("t1")))
               .andExpect(jsonPath("$[0].type.description", is("t1v")))
               .andExpect(jsonPath("$[0].amount.amount", is("-200.00")))
               .andExpect(jsonPath("$[0].amount.currency", is("EUR")))
               .andExpect(jsonPath("$[0].description", is("transaction 1 description")))
               .andExpect(jsonPath("$[1].transactionId", is("t2")))
               .andExpect(jsonPath("$[1].operationId", is("op2")))
               .andExpect(jsonPath("$[1].accountingDate", is("2022-11-22")))
               .andExpect(jsonPath("$[1].valueDate", is("2022-11-24")))
               .andExpect(jsonPath("$[1].type.enumeration", is("t2")))
               .andExpect(jsonPath("$[1].type.description", is("t2v")))
               .andExpect(jsonPath("$[1].amount.amount", is("1200.00")))
               .andExpect(jsonPath("$[1].amount.currency", is("EUR")))
               .andExpect(jsonPath("$[1].description", is("transaction 2 description"))
               );
    }

    @TestConfiguration
    static class TransactionsControllerTestConfiguration {

        private static final String EUR = "EUR";

        @Bean
        public TransactionsService transactionsService() {
            return new TransactionsService(new TransactionsRepository() {
                @Override
                public List<Transaction> transactions(Long accountId, DateInterval dateInterval)
                    throws AccountNotFoundException, InformationUnavailableException {
                    if (accountId == ACCOUNT_NOT_FOUND_ID) {
                        throw new AccountNotFoundException(accountId);
                    } else if (accountId == ACCOUNT_WITH_TRANSACTIONS_ID) {
                        return List.of(
                            Transaction
                                .builder()
                                .transactionId("t1")
                                .operationId("op1")
                                .accountingDate(LocalDate.parse("2022-11-21"))
                                .valueDate(LocalDate.parse("2022-11-23"))
                                .transactionType(new Transaction.TransactionType("t1", "t1v"))
                                .amount(new Amount(-20000L, Currency.getInstance(EUR)))
                                .description("transaction 1 description")
                                .build(),
                            Transaction
                                .builder()
                                .transactionId("t2")
                                .operationId("op2")
                                .accountingDate(LocalDate.parse("2022-11-22"))
                                .valueDate(LocalDate.parse("2022-11-24"))
                                .transactionType(new Transaction.TransactionType("t2", "t2v"))
                                .amount(new Amount(120000L, Currency.getInstance(EUR)))
                                .description("transaction 2 description")
                                .build()
                        );
                    }
                    throw new InformationUnavailableException(accountId, dateInterval);
                }
            });
        }
    }
}