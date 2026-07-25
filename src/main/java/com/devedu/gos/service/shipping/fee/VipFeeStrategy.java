package com.devedu.gos.service.shipping.fee;

import com.devedu.gos.model.ShippingTypeEnum;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class VipFeeStrategy implements FeeStrategy {
    @Override
    public BigDecimal calculateFee(BigDecimal amountInBrl) {
        return BigDecimal.ZERO;
    }

    @Override
    public ShippingTypeEnum getShippingType() {
        return ShippingTypeEnum.VIP;
    }
}
