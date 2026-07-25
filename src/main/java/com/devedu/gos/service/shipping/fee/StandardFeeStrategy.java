package com.devedu.gos.service.shipping.fee;

import com.devedu.gos.model.ShippingTypeEnum;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StandardFeeStrategy implements FeeStrategy {
    @Override
    public BigDecimal calculateFee(BigDecimal amountInBrl) {
        return amountInBrl.multiply(new BigDecimal("0.05"));
    }

    @Override
    public ShippingTypeEnum getShippingType() {
        return ShippingTypeEnum.STANDARD;
    }
}
