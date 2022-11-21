package it.corradolombardi.fabanking.fabrikclient;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class FabrikApiStatusCodeException extends FabrikApiException {

    private final HttpStatus statusCode;
    private final FabrikError fabrikError;

    public FabrikApiStatusCodeException(HttpStatusCodeException httpStatusCodeException) {
        super(httpStatusCodeException);
        statusCode = httpStatusCodeException.getStatusCode();
        fabrikError = new Gson().fromJson(httpStatusCodeException
                .getResponseBodyAsString(), FabrikError.class);
    }

    public boolean userUnknown() {
        return HttpStatus.FORBIDDEN.equals(statusCode);
    }
}
