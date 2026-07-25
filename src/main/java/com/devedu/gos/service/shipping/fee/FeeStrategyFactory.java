package com.devedu.gos.service.shipping.fee;

import com.devedu.gos.model.ShippingTypeEnum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FeeStrategyFactory {

    private final Map<String, FeeStrategy> strategies;

    public FeeStrategyFactory(List<FeeStrategy> strategyList) {
        this.strategies = strategyList.stream().collect(Collectors.toMap(s ->
                s.getShippingType().toString().toUpperCase(),
                Function.identity()
        ));
    }

    public FeeStrategy getStrategy(ShippingTypeEnum shippingType) {
        FeeStrategy strategy = strategies.get(shippingType.toString().toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Tipo de envio inválido: " + shippingType);
        }
        return strategy;
    }
}
