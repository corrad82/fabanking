package it.corradolombardi.fabanking.fabrikclient;

import java.util.List;

import com.google.gson.Gson;

public class NoPayloadFabrikApiResponse<Void> extends FabrikApiResponse<Void> {
    public NoPayloadFabrikApiResponse(String status, List<FabrikError> errors) {
        super(status, errors, null);
    }

    public static NoPayloadFabrikApiResponse fromJson(String json) {
        return new Gson().fromJson(json, NoPayloadFabrikApiResponse.class);
    }
}
