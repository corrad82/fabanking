package it.corradolombardi.fabanking.model;

import it.corradolombardi.fabanking.rest.AddressRest;
import lombok.Data;

@Data
public class Address {
    private final String address;
    private final String city;
    private final String countryCode;

    public AddressRest toRest() {
        return new AddressRest(address, city, countryCode);
    }
}
