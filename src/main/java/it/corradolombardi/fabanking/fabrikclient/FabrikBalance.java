package it.corradolombardi.fabanking.fabrikclient;

import com.google.gson.Gson;
import lombok.Data;

@Data
public class FabrikBalance {

    private final String date;
    private final String balance;
    private final String availableBalance;
    private final String currency;

    public static FabrikBalance fromJson(String jsonResponse) {
        return new Gson().fromJson(jsonResponse, FabrikBalance.class);
    }

    public static FabrikBalance nullValues() {
        return new FabrikBalance(null, null, null, null);
    }

}
