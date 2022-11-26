package it.corradolombardi.fabanking.model;

import it.corradolombardi.fabanking.rest.PersonRest;
import lombok.Data;

@Data
public class Person {
    private final String name;
    private final Account account;
    private final Address address;

    public PersonRest toRest() {
        return new PersonRest(name,
                              account.toRest(),
                              address.toRest());
    }
}
