package de.yagub.deliverysystem.msorder.dto.request;

import java.math.BigDecimal;

public record OrderItemRequest(
     String productId,
     int quantity,
     BigDecimal pricePerUnit
) { }
