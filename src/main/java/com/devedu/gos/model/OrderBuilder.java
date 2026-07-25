package com.devedu.gos.model;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderBuilder {

    @Autowired
    private Order order;

    private OrderBuilder() {
        this.order = new Order();
        this.order.setCreatedAt(LocalDateTime.now());
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public OrderBuilder customerName(String name) {
        this.order.setCustomerName(name);
        return this;
    }

    public OrderBuilder currency(String currency) {
        this.order.setCurrency(currency);
        return this;
    }

    public OrderBuilder originalAmount(BigDecimal amount) {
        this.order.setOriginalAmount(amount);
        return this;
    }

    public OrderBuilder exchangeRate(BigDecimal rate) {
        this.order.setExchangeRate(rate);
        return this;
    }

    public OrderBuilder shippingType(ShippingTypeEnum type) {
        this.order.setShippingType(type);
        return this;
    }

    public OrderBuilder calculateTotals(BigDecimal fee) {
        BigDecimal converted = this.order.getOriginalAmount().multiply(this.order.getExchangeRate());
        this.order.setConvertedAmount(converted);
        this.order.setFee(fee);
        this.order.setFinalAmount(converted.add(fee));
        return this;
    }

    public Order build() {
        if (this.order.getCustomerName() == null || this.order.getOriginalAmount() == null) {
            throw new IllegalStateException("Cliente e valor original são obrigatórios.");
        }
        return this.order;
    }
}
