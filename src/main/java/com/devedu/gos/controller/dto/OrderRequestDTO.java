package com.devedu.gos.controller.dto;

import java.math.BigDecimal;

public record OrderRequestDTO(String customerName,
                              String currency,
                              BigDecimal amount,
                              String shippingType) {
}
