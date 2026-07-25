package com.devedu.gos.client.currency.impl;

import com.devedu.gos.client.currency.ExchangeRateAdapter;
import com.devedu.gos.client.dto.AwesomeApiCurrencyResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class AwesomeExchangeRateAdapterImpl implements ExchangeRateAdapter {

    private final RestClient restClient;

    public AwesomeExchangeRateAdapterImpl(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("https://economia.awesomeapi.com.br/last")
                .build();
    }

    @Override
    public BigDecimal getExchangeRate(String fromCurrency) {
        if ("BRL".equalsIgnoreCase(fromCurrency)) {
            return BigDecimal.ONE;
        }

        String pair = fromCurrency.toUpperCase() + "-BRL";

        Map<String, AwesomeApiCurrencyResponse> response = restClient
                .get()
                .uri("/{pair}", pair)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (response != null && response.containsKey(fromCurrency.toUpperCase() + "BRL")) {
            String bidRate = response.get(fromCurrency.toUpperCase() + "BRL").bid();

            return new BigDecimal(bidRate);
        }

        throw new IllegalArgumentException("Moeda não suportada ou cotação indisponível: " + fromCurrency);
    }
}
