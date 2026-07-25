package com.devedu.gos.controller.dto;

import com.devedu.gos.model.ShippingTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponseDTO(Long id,
                               String customerName,
                               String currency,
                               BigDecimal originalAmount,
                               BigDecimal exchangeRate,
                               BigDecimal convertedAmount,
                               BigDecimal fee,
                               BigDecimal finalAmount,
                               ShippingTypeEnum shippingType,
                               LocalDateTime createdAt) {
}
