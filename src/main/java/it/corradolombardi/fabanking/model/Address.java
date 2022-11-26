package it.corradolombardi.fabanking.model;

import lombok.Data;

@Data
public class Address {
    private final String address;
    private final String city;
    private final String countryCode;
}
