package it.corradolombardi.fabanking.rest;

import lombok.Data;

@Data
public class AddressRest {

    private final String address;
    private final String city;
    private final String countryCode;

}
