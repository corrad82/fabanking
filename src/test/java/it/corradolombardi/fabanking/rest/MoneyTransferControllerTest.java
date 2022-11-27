package it.corradolombardi.fabanking.rest;

import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.function.Supplier;

import com.google.gson.Gson;
import it.corradolombardi.fabanking.model.Account;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.Address;
import it.corradolombardi.fabanking.model.Amount;
import it.corradolombardi.fabanking.model.MoneyTransfer;
import it.corradolombardi.fabanking.model.MoneyTransferException;
import it.corradolombardi.fabanking.model.MoneyTransferFee;
import it.corradolombardi.fabanking.model.MoneyTransferRequest;
import it.corradolombardi.fabanking.model.Person;
import it.corradolombardi.fabanking.model.TransferAmounts;
import it.corradolombardi.fabanking.moneytransfer.MoneyTransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

@WebMvcTest(MoneyTransferController.class)
class MoneyTransferControllerTest {

    private static final Currency EUR = Currency.getInstance("EUR");
    private static final String MONEY_TRANSFER_RESOURCE = "/money-transfer";
    private static final long VALID_ACCOUNT_ID = 34534L;
    private static final long NOT_FOUND_ACCOUNT_ID = 404L;
    @Autowired
    private MockMvc mockMvc;

    private final Gson gson = new Gson();

    @Test
    void transferPerformed() throws Exception {

        Long accountId = VALID_ACCOUNT_ID;
        String receiverName = "john smith";
        String description = "wire transfer to john smith";
        Amount amount = new Amount(120000L, EUR);
        String executionDate = "2022-11-28";

        String requestJson = request(accountId, receiverName, description, amount, executionDate);

        mockMvc.perform(post(MONEY_TRANSFER_RESOURCE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(requestJson))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.debtor.account.accountCode", is(accountId.toString())))
               .andExpect(jsonPath("$.creditor.name", is(receiverName)))
               .andExpect(jsonPath("$.description", is(description)))
               .andExpect(jsonPath("$.amounts.debtorAmount.amount", is("1200.00")))
               .andExpect(jsonPath("$.amounts.debtorAmount.currency", is("EUR")))
               .andExpect(jsonPath("$.amounts.creditorAmount.amount", is("1200.00")))
               .andExpect(jsonPath("$.amounts.creditorAmount.currency", is("EUR")))
               .andExpect(jsonPath("$.debtorValueDate", is(executionDate)));
    }


    @Test
    void wrongContentNegotiation() throws Exception {
        Long accountId = VALID_ACCOUNT_ID;
        String receiverName = "john smith";
        String description = "wire transfer to john smith";
        Amount amount = new Amount(120000L, EUR);
        String executionDate = "2022-11-28";

        String requestJson = request(accountId, receiverName, description, amount, executionDate);

        mockMvc.perform(post(MONEY_TRANSFER_RESOURCE)
                            .contentType(MediaType.APPLICATION_XML)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(requestJson))
               .andExpect(status().isUnsupportedMediaType());

        mockMvc.perform(post(MONEY_TRANSFER_RESOURCE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_XML)
                            .content(requestJson))
               .andExpect(status().isNotAcceptable());

        mockMvc.perform(post(MONEY_TRANSFER_RESOURCE)
                            .contentType(MediaType.APPLICATION_XML)
                            .accept(MediaType.APPLICATION_XML)
                            .content(requestJson))
               .andExpect(status().isUnsupportedMediaType());
    }


    @Test
    void notPostHttpVerbsUnsupported() throws Exception {
        testMethodNotAllowed(() -> get(MONEY_TRANSFER_RESOURCE));
        testMethodNotAllowed(() -> patch(MONEY_TRANSFER_RESOURCE));
        testMethodNotAllowed(() -> put(MONEY_TRANSFER_RESOURCE));
        testMethodNotAllowed(() -> delete(MONEY_TRANSFER_RESOURCE));
    }

    @Test
    void accountNotFound() throws Exception {
        Long accountId = NOT_FOUND_ACCOUNT_ID;
        String receiverName = "john smith";
        String description = "wire transfer to john smith";
        Amount amount = new Amount(120000L, EUR);
        String executionDate = "2022-11-28";

        String requestJson = request(accountId, receiverName, description, amount, executionDate);

        mockMvc.perform(post(MONEY_TRANSFER_RESOURCE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(requestJson))
               .andExpect(status().isNotFound());
    }

    @Test
    void invalidRequest() throws Exception {
        Long accountId = NOT_FOUND_ACCOUNT_ID;

        String requestJson = request(accountId, null, null, null, null);

        mockMvc.perform(post(MONEY_TRANSFER_RESOURCE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(requestJson))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.error",
                                   is("All request parameters: accountId, receiverName, description, amount, " +
                                          "currency, execution date must be set")));
    }

    @Test
    void errorDuringTransferExecution() throws Exception {
        Long accountId = 555L;
        String receiverName = "john smith";
        String description = "wire transfer to john smith";
        Amount amount = new Amount(120000L, EUR);
        String executionDate = "2022-11-28";

        String requestJson = request(accountId, receiverName, description, amount, executionDate);

        mockMvc.perform(post(MONEY_TRANSFER_RESOURCE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(requestJson))
               .andExpect(status().isInternalServerError())
               .andExpect(jsonPath("$.error",
                                   is("Error while transfering money from account 555: something went wrong")));
    }

    private void testMethodNotAllowed(Supplier<RequestBuilder> rbs) throws Exception {
        mockMvc.perform(rbs.get())
               .andExpect(status().isMethodNotAllowed());
    }


    private String request(Long accountId, String receiverName, String description, Amount amount,
                           String executionDate) {
        MoneyTransferRequest request = new MoneyTransferRequest(accountId,
                                                                receiverName,
                                                                description,
                                                                amount,
                                                                executionDate);
        return gson.toJson(request, MoneyTransferRequest.class);
    }

    @TestConfiguration
    static class MoneyTransferControllerTestConfiguration {


        @Bean
        public MoneyTransferService moneyTransferService() {
            return new MoneyTransferService(request -> {
                if (request.getAccountId() == VALID_ACCOUNT_ID) {
                    Person creditor = new Person("john smith", new Account("a", "b"),
                                                 new Address("viale vittoria", "Milano", "IT"));

                    Person debtor = new Person("john doe", new Account("34534", "d"),
                                               new Address("viale piacenza", "Milano", "IT"));

                    Amount transferredAmount = new Amount(120000L, EUR);
                    TransferAmounts amounts =
                        new TransferAmounts(transferredAmount, transferredAmount, LocalDate.parse("2022-11-29"),
                                            1.0);
                    List<MoneyTransferFee> fees = singletonList(
                        new MoneyTransferFee("feecode", "transfer fee 1",
                                             new Amount(85L, EUR)));

//                    return new MoneyTransfer("1234", "EXECUTED", MoneyTransfer.Direction.OUTGOING,
//                                             creditor, debtor, "cro",
//                                             "uri", "trn",
//                                             "wire transfer to john smith",
//                                             LocalDate.parse("2022-11-26").atTime(22, 0),
//                                             LocalDate.parse("2022-11-28").atTime(10, 0),
//                                             LocalDate.parse("2022-11-28"),
//                                             LocalDate.parse("2022-11-29"),
//                                             amounts,
//                                             false,
//                                             false,
//                                             "SHA",
//                                             "1123",
//                                             fees,
//                                             false);

                    return MoneyTransfer.builder().moneyTransferId("1234")
                                        .status("EXECUTED")
                                        .direction(MoneyTransfer.Direction.OUTGOING)
                                        .creditor(creditor)
                                        .debtor(debtor)
                                        .cro("cro").uri("uri").trn("trn")
                                        .description("wire transfer to john smith")
                                        .createdDatetime(LocalDate.parse("2022-11-26").atTime(22, 0))
                                        .accountedDatetime(LocalDate.parse("2022-11-28").atTime(10, 0))
                                        .debtorValueDate(LocalDate.parse("2022-11-28"))
                                        .creditorValueDate(LocalDate.parse("2022-11-29"))
                                        .amounts(amounts)
                                        .isUrgent(false).isInstant(false).feeType("SHA").feeAccountId("1123")
                                        .fees(fees).hasTaxRelief(false).build();
                }
                if (request.getAccountId() == NOT_FOUND_ACCOUNT_ID) {
                    throw new AccountNotFoundException(NOT_FOUND_ACCOUNT_ID);
                }
                throw new MoneyTransferException(request.getAccountId(), "something went wrong");
            });
        }

    }
}