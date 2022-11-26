package it.corradolombardi.fabanking.moneytransfer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import it.corradolombardi.fabanking.model.Account;
import it.corradolombardi.fabanking.model.AccountNotFoundException;
import it.corradolombardi.fabanking.model.Address;
import it.corradolombardi.fabanking.model.Amount;
import it.corradolombardi.fabanking.model.MoneyTransfer;
import it.corradolombardi.fabanking.model.MoneyTransfer.Direction;
import it.corradolombardi.fabanking.model.MoneyTransferException;
import it.corradolombardi.fabanking.model.MoneyTransferFee;
import it.corradolombardi.fabanking.model.MoneyTransferRequest;
import it.corradolombardi.fabanking.model.Person;
import it.corradolombardi.fabanking.model.TransferAmounts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MoneyTransferServiceTest {

    private static final Currency EUR = Currency.getInstance("EUR");
    @Mock
    private MoneyTransferRepository moneyTransferRepository;
    private MoneyTransferService moneyTransferService;

    @BeforeEach
    void setUp() {
        moneyTransferService = new MoneyTransferService(moneyTransferRepository);
    }

    @Test
    void moneyTransferOk() throws Exception {

        Long accountId = 234L;
        String receiverName = "John Doe";
        String description = "this is a test transfer";
        Amount amount = new Amount(12000L, EUR);
        String executionDate = "2022-11-25";


        MoneyTransferRequest request = new MoneyTransferRequest(accountId,
                                                                receiverName,
                                                                description,
                                                                amount,
                                                                executionDate);

        when(moneyTransferRepository.transfer(request))
            .thenReturn(moneyTransfer(accountId, receiverName, description, amount, executionDate));

        MoneyTransfer moneyTransfer = moneyTransferService.transfer(request);

        assertEquals(
            moneyTransfer,
            moneyTransfer(accountId, receiverName, description, amount, executionDate));
    }

    @Test
    void invalidRequest() {

        MoneyTransferRequest request = new MoneyTransferRequest(123L, "receiver",
                                                                null, null, "2022-11-26");

        assertThrows(IllegalArgumentException.class, () -> moneyTransferService.transfer(request));

        verifyNoInteractions(moneyTransferRepository);

    }

    @Test
    void negativeAccount() {

        MoneyTransferRequest request = new MoneyTransferRequest(-123L, "receiver",
                                                                "description",
                                                                new Amount(400L, EUR),
                                                                "2022-11-24");

        assertThrows(AccountNotFoundException.class, () -> moneyTransferService.transfer(request));

        verifyNoInteractions(moneyTransferRepository);

    }

    @Test
    void exceptionThrownByRepository() throws Exception {

        MoneyTransferRequest request = new MoneyTransferRequest(123L, "receiver",
                                                                "description",
                                                                new Amount(400L, EUR),
                                                                "2022-11-26");

        doThrow(MoneyTransferException.class)
            .when(moneyTransferRepository).transfer(request);

        assertThrows(MoneyTransferException.class, () -> moneyTransferService.transfer(request));


    }

    private MoneyTransfer moneyTransfer(Long accountId, String receiverName, String description, Amount amount,
                                        String executionDate) {
        Person creditor = new Person(receiverName,
                                     new Account("accountcode", "bic"),
                                     new Address("parco della vittoria", "Monopoli", "IT"));

        Person debtor = new Person(receiverName,
                                   new Account("accountcode", "bic"),
                                   new Address("parco della vittoria", "Monopoli", "IT"));


        LocalDate localExecutionDate = LocalDate.parse(executionDate);
        TransferAmounts amounts = new TransferAmounts(amount, amount, localExecutionDate, 1.0);
        List<MoneyTransferFee> fees = List.of(
            new MoneyTransferFee("aaa", "fee1", new Amount(25L, EUR)),
            new MoneyTransferFee("bbb", "fee2", new Amount(55L, EUR))
        );
        return new MoneyTransfer("123", "EXECUTED", Direction.OUTGOING,
                                 creditor, debtor,
                                 "cro", "uri", "trn",
                                 description,
                                 localExecutionDate.atTime(2, 0),
                                 localExecutionDate.atTime(15, 0),
                                 localExecutionDate,
                                 localExecutionDate.plusDays(1L),
                                 amounts,
                                 false,
                                 false,
                                 "SHA",
                                 "12345678",
                                 fees,
                                 false);
    }
}