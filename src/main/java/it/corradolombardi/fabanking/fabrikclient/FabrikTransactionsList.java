package it.corradolombardi.fabanking.fabrikclient;

import java.util.List;

import lombok.Data;

@Data
public class FabrikTransactionsList {
    private final List<FabrikTransaction> list;
}
