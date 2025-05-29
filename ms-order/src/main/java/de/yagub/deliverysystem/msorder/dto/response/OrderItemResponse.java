package de.yagub.deliverysystem.msorder.dto.response;

import java.math.BigDecimal;

public record OrderItemResponse(
        String productId,
        int quantity,
        BigDecimal pricePerUnit
) { }
