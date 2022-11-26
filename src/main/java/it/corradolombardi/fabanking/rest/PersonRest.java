package it.corradolombardi.fabanking.rest;

import lombok.Data;

@Data
public class PersonRest {
    private final String name;
    private final AccountRest account;
    private final AddressRest address;
}
