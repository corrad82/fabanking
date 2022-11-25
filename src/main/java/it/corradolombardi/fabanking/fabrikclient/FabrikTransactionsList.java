package it.corradolombardi.fabanking.fabrikclient;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FabrikTransactionsList {
    private List<FabrikTransaction> list;

    public static FabrikTransactionsList nullList() {
        return new FabrikTransactionsList(null);
    }
}
