package it.corradolombardi.fabanking.model;

import lombok.Data;

@Data
public class Person {
    private final String name;
    private final Account account;
    private final Address address;
}
