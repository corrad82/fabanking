package it.corradolombardi.fabanking.fabrikclient;

import java.util.List;

public class TransactionsFabrikResponse extends BaseFabrikResponse<FabrikTransactionsList> {

    public TransactionsFabrikResponse(String status, List errors, FabrikTransactionsList payload) {
        super(status, errors, payload);
    }
}
