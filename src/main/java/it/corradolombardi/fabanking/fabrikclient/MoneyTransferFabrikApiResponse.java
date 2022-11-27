package it.corradolombardi.fabanking.fabrikclient;

import java.util.List;

public class MoneyTransferFabrikApiResponse extends FabrikApiResponse<FabrikMoneyTransfer> {

    public MoneyTransferFabrikApiResponse(String status, List<FabrikError> errors, FabrikMoneyTransfer payload) {
        super(status, errors, payload);
    }
}
