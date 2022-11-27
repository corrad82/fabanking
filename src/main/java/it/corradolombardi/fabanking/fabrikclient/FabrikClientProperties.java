package it.corradolombardi.fabanking.fabrikclient;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "fabrik.client.banking-account-cash")
@Getter
@Setter
public class FabrikClientProperties {

    private String baseUrl;
    private String authSchema;
    private String apiKey;

}
