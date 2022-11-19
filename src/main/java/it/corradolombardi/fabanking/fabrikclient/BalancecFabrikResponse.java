package it.corradolombardi.fabanking.fabrikclient;

import com.google.gson.Gson;

import java.util.List;

public class BalancecFabrikResponse extends BaseFabrikResponse<FabrikBalance> {


    public BalancecFabrikResponse(String status, List<FabrikError> errors, FabrikBalance payload) {
        super(status, errors, payload);
    }

    public static BalancecFabrikResponse fromJson(String json) {
        return new Gson().fromJson(json, BalancecFabrikResponse.class);
    }
}
