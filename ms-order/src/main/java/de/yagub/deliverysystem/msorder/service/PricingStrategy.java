package de.yagub.deliverysystem.msorder.service;

import de.yagub.deliverysystem.msorder.dto.request.OrderRequest;
import de.yagub.deliverysystem.msorder.model.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculatePrice(OrderRequest orderRequest);
    boolean isSuitable(OrderRequest orderRequest);
    int filterId();
}
