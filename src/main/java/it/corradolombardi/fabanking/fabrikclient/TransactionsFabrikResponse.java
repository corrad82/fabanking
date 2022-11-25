package it.corradolombardi.fabanking.fabrikclient;

import java.util.List;

import com.google.gson.Gson;

public class TransactionsFabrikResponse extends BaseFabrikResponse<FabrikTransactionsList> {

    public TransactionsFabrikResponse(String status, List errors, FabrikTransactionsList payload) {
        super(status, errors, payload);
    }

    // FIXME: this method can be pulled up
    public static TransactionsFabrikResponse fromJson(String json) {
        return new Gson().fromJson(json, TransactionsFabrikResponse.class);
    }
}
