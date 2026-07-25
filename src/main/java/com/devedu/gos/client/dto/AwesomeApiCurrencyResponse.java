package com.devedu.gos.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AwesomeApiCurrencyResponse(@JsonProperty("bid") String bid) {
}
