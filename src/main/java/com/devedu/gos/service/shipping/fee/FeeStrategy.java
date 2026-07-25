package com.devedu.gos.service.shipping.fee;

import com.devedu.gos.model.ShippingTypeEnum;

import java.math.BigDecimal;

public interface FeeStrategy {

    BigDecimal calculateFee(BigDecimal amountInBrl);

    ShippingTypeEnum getShippingType();
}
