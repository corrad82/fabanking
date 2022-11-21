package it.corradolombardi.fabanking.fabrikclient;

import java.util.List;

import com.google.gson.Gson;

public class NoPayloadFabrikResponse<Void> extends BaseFabrikResponse<Void> {
    public NoPayloadFabrikResponse(String status, List<FabrikError> errors) {
        super(status, errors, null);
    }

    public static NoPayloadFabrikResponse fromJson(String json) {
        return new Gson().fromJson(json, NoPayloadFabrikResponse.class);
    }
}
