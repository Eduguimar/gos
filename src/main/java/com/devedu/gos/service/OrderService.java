package com.devedu.gos.service;

import com.devedu.gos.client.currency.ExchangeRateAdapter;
import com.devedu.gos.controller.dto.OrderRequestDTO;
import com.devedu.gos.controller.dto.OrderResponseDTO;
import com.devedu.gos.model.Order;
import com.devedu.gos.model.OrderBuilder;
import com.devedu.gos.repository.OrderRepository;
import com.devedu.gos.service.shipping.fee.FeeStrategy;
import com.devedu.gos.service.shipping.fee.FeeStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository repository;
    @Autowired
    ExchangeRateAdapter exchangeRateAdapter;
    @Autowired
    FeeStrategyFactory feeStrategyFactory;

    public OrderService(OrderRepository repository,
                        ExchangeRateAdapter exchangeRateAdapter,
                        FeeStrategyFactory feeStrategyFactory) {
        this.repository = repository;
        this.exchangeRateAdapter = exchangeRateAdapter;
        this.feeStrategyFactory = feeStrategyFactory;
    }

    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        BigDecimal rate = exchangeRateAdapter.getExchangeRate(orderRequestDTO.currency());

        BigDecimal baseConverted = orderRequestDTO.amount().multiply(rate);
        FeeStrategy strategy = feeStrategyFactory.getStrategy(orderRequestDTO.shippingType());
        BigDecimal fee = strategy.calculateFee(baseConverted);

        Order order = OrderBuilder.builder()
                .customerName(orderRequestDTO.customerName())
                .currency(orderRequestDTO.currency())
                .originalAmount(orderRequestDTO.amount())
                .exchangeRate(rate)
                .shippingType(orderRequestDTO.shippingType())
                .calculateTotals(fee)
                .build();

        Order saved = repository.save(order);
        return toResponseDTO(saved);
    }

    public List<OrderResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private OrderResponseDTO toResponseDTO(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getCustomerName(),
                order.getCurrency(),
                order.getOriginalAmount(),
                order.getExchangeRate(),
                order.getConvertedAmount(),
                order.getFee(),
                order.getFinalAmount(),
                order.getShippingType(),
                order.getCreatedAt()
        );
    }
}
