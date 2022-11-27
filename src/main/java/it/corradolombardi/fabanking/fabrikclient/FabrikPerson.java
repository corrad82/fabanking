package it.corradolombardi.fabanking.fabrikclient;

import lombok.Data;

@Data
public class FabrikPerson {
    private final String name;
    private final FabrikAccount account;
    private final FabrikAddress address;
}
