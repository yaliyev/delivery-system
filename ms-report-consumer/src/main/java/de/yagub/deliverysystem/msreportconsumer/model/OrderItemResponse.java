package de.yagub.deliverysystem.msreportconsumer.model;

import java.math.BigDecimal;

public record OrderItemResponse(
        String productId,
        int quantity,
        BigDecimal pricePerUnit
) { }
