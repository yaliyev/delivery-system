package de.yagub.deliverysystem.msprocessmanager.client.order.model;

import java.math.BigDecimal;

public record OrderItemResponse(
        String productId,
        int quantity,
        BigDecimal pricePerUnit
) { }
