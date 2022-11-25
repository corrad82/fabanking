package it.corradolombardi.fabanking.fabrikclient;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FabrikClientConfiguration {

    @Bean
    public FabrikClient fabrikClient(FabrikBankingAccountProperties properties,
                                     RestTemplateBuilder restTemplateBuilder) {

        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.getInterceptors()
                .add((request, body, execution) -> {
                    request.getHeaders()
                            .add("Auth-Schema", properties.getAuthSchema());
                    request.getHeaders()
                            .add("Api-Key", properties.getApiKey());

                    return execution.execute(request, body);
                });
        return new FabrikClient(properties.getBaseUrl(), restTemplate);
    }
}
