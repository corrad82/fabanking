package it.corradolombardi.fabanking.fabrikclient;

import lombok.Data;

@Data
public class FabrikAddress {
    private final String address;
    private final String city;
    private final String countryCode;
}
