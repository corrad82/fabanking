package it.corradolombardi.fabanking.fabrikclient;

import java.util.List;

import com.google.gson.Gson;

public class TransactionsFabrikApiResponse extends FabrikApiResponse<FabrikTransactionsList> {

    public TransactionsFabrikApiResponse(String status, List errors, FabrikTransactionsList payload) {
        super(status, errors, payload);
    }

    // FIXME: this method can be pulled up
    public static TransactionsFabrikApiResponse fromJson(String json) {
        return new Gson().fromJson(json, TransactionsFabrikApiResponse.class);
    }
}
