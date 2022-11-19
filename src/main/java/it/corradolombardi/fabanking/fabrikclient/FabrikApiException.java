package it.corradolombardi.fabanking.fabrikclient;

import org.springframework.web.client.RestClientException;

public class FabrikApiException extends Throwable {
    public FabrikApiException(Exception e) {
        super(e);
    }
}
