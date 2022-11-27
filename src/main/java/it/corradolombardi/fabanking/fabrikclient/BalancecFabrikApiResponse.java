package it.corradolombardi.fabanking.fabrikclient;

import com.google.gson.Gson;

import java.util.List;

public class BalancecFabrikApiResponse extends FabrikApiResponse<FabrikBalance> {


    public BalancecFabrikApiResponse(String status, List<FabrikError> errors, FabrikBalance payload) {
        super(status, errors, payload);
    }

    public static BalancecFabrikApiResponse fromJson(String json) {
        return new Gson().fromJson(json, BalancecFabrikApiResponse.class);
    }
}
