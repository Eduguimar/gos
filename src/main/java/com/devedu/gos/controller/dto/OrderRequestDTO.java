package com.devedu.gos.controller.dto;

import com.devedu.gos.model.ShippingTypeEnum;

import java.math.BigDecimal;

public record OrderRequestDTO(String customerName,
                              String currency,
                              BigDecimal amount,
                              ShippingTypeEnum shippingType) {
}
