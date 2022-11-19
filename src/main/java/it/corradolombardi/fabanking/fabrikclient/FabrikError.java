package it.corradolombardi.fabanking.fabrikclient;

import lombok.Data;

@Data
public class FabrikError {
    private final String code;
    private final String description;
    private final String params;
}
