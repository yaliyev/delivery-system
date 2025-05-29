package de.yagub.deliverysystem.msprocessmanager.client.order.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(

        String customerId,
        List<OrderItemResponse> items,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime createdAt
) { }
