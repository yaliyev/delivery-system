package de.yagub.deliverysystem.msorder.dto.response;

import de.yagub.deliverysystem.msorder.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
    String id,
    String customerId,
    List<OrderItemResponse> items,
    BigDecimal totalAmount,
    OrderStatus status,
    LocalDateTime createdAt
) { }
