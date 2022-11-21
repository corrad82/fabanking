package it.corradolombardi.fabanking.fabrikclient;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class FabrikApiStatusCodeException extends FabrikApiException {

    private final HttpStatus statusCode;
    private final NoPayloadFabrikResponse fabrikResponse;

    public FabrikApiStatusCodeException(HttpStatusCodeException httpStatusCodeException) {
        super(httpStatusCodeException);
        statusCode = httpStatusCodeException.getStatusCode();
        fabrikResponse = NoPayloadFabrikResponse.fromJson(
            httpStatusCodeException.getResponseBodyAsString()
        );
    }

    public boolean userUnknown() {
        return HttpStatus.FORBIDDEN.equals(statusCode)
            && fabrikResponse.isInvalidAccount();
    }

}
