package it.corradolombardi.fabanking.fabrikclient;

import org.springframework.web.client.RestClientException;

public class FabrikApiException extends Exception {

    public FabrikApiException(RestClientException e) {
        super(e);
    }


}
