package com.devedu.gos.client.currency;

import java.math.BigDecimal;

public interface ExchangeRateAdapter {
    BigDecimal getExchangeRate(String fromCurrency);
}
