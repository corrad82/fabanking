package it.corradolombardi.fabanking.moneytransfer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.stream.Collectors;

import it.corradolombardi.fabanking.fabrikclient.FabrikAccount;
import it.corradolombardi.fabanking.fabrikclient.FabrikClient;
import it.corradolombardi.fabanking.fabrikclient.FabrikAddress;
import it.corradolombardi.fabanking.fabrikclient.FabrikAmount;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiException;
import it.corradolombardi.fabanking.fabrikclient.FabrikApiStatusCodeException;
import it.corradolombardi.fabanking.fabrikclient.FabrikFee;
import it.corradolombardi.fabanking.fabrikclient.FabrikPerson;
import it.corradolombardi.fabanking.fabrikclient.FabrikMoneyTransfer;
import it.corradolombardi.fabanking.fabrikclient.FabrikMoneyTransferRequest;
import it.corradolombardi.fabanking.fabrikclient.MoneyTransferFabrikApiResponse;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiClientMoneyTransferRepository
    implements MoneyTransferRepository {

    private final FabrikClient fabrikClient;

    public ApiClientMoneyTransferRepository(FabrikClient fabrikClient) {
        this.fabrikClient = fabrikClient;
    }

    @Override
    public MoneyTransfer transfer(MoneyTransferRequest request)
        throws AccountNotFoundException, MoneyTransferException {

        FabrikPerson fabrikPerson = new FabrikPerson(request.getReceiverName(),
                                                     new FabrikAccount("", ""),
                                                     new FabrikAddress("", "", ""));
        FabrikMoneyTransferRequest fabrikRequest = FabrikMoneyTransferRequest
            .builder()
            .creditor(fabrikPerson)
            .description(request.getDescription())
            .currency(request.getAmount().getCurrency())
            .amount(String.valueOf(request.getAmount().getAmount()))
            .executionDate(request.getExecutionDate())
            .build();
        try {
            MoneyTransferFabrikApiResponse response =
                fabrikClient.moneyTransfer(request.getAccountId(), fabrikRequest);
            if (response.isOk()) {
                return toMoneyTransfer(response.getPayload());
            }
        } catch (FabrikApiStatusCodeException e) {
            if (e.userUnknown()) {
                throw new AccountNotFoundException(request.getAccountId());
            }
        } catch (FabrikApiException e) {
            log.error(e.getMessage(), e);
        }
        throw new MoneyTransferException(request.getAccountId(),
                                         "Unable to perform money transfer, please try again later");
    }

    private MoneyTransfer toMoneyTransfer(FabrikMoneyTransfer response) {
        return MoneyTransfer.builder()
                            .moneyTransferId(response.getMoneyTransferId())
                            .status(response.getStatus())
                            .direction(MoneyTransfer.Direction.valueOf(response.getDirection()))
                            .creditor(person(response.getCreditor()))
                            .debtor(person(response.getDebtor()))
                            .cro(response.getCro())
                            .trn(response.getTrn())
                            .uri(response.getUri())
                            .description(response.getDescription())
                            .createdDatetime(LocalDateTime.parse(response.getCreatedDatetime()))
                            .accountedDatetime(LocalDateTime.parse(response.getAccountedDatetime()))
                            .debtorValueDate(LocalDate.parse(response.getDebtorValueDate()))
                            .creditorValueDate(LocalDate.parse(response.getCreditorValueDate()))
                            .amounts(amounts(response.getAmount()))
                            .isUrgent(response.isUrgent())
                            .isInstant(response.isInstant())
                            .feeType(response.getFeeType())
                            .feeAccountId(response.getFeeAccountID())
                            .fees(response.getFees().stream().map(this::fee).collect(Collectors.toList()))
                            .hasTaxRelief(response.isHasTaxRelief())
                            .build();
    }

    private MoneyTransferFee fee(FabrikFee fee) {
        return new MoneyTransferFee(fee.getFeeCode(),
                                    fee.getDescription(),
                                    new Amount(cents(fee.getAmount()), Currency.getInstance(fee.getCurrency())));
    }

    private TransferAmounts amounts(FabrikAmount amount) {
        Amount debtorAmount =
            new Amount(cents(amount.getDebtorAmount()), Currency.getInstance(amount.getDebtorCurrency()));
        Amount creditorAmount = new Amount(cents(amount.getCreditorAmount()),
                                           Currency.getInstance(amount.getCreditorCurrency()));
        return new TransferAmounts(debtorAmount, creditorAmount,
                                   LocalDate.parse(amount.getCreditorCurrencyDate()),
                                   amount.getExchangeRate());
    }

    private Person person(FabrikPerson fabrikPerson) {
        FabrikAccount fabrikAccount = fabrikPerson.getAccount();
        Account account = new Account(fabrikAccount.getAccountCode(),
                                      fabrikAccount.getBicCode());
        FabrikAddress fabrikAddress = fabrikPerson.getAddress();
        Address address = new Address(fabrikAddress.getAddress(), fabrikAddress.getCity(),
                                      fabrikAddress.getCountryCode());
        return new Person(fabrikPerson.getName(),
                          account, address);
    }

    private Long cents(Double value) {
        return ((Double) (value * 100)).longValue();
    }

}
