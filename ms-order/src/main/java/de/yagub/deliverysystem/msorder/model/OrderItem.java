package de.yagub.deliverysystem.msorder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    private String productId;
    private int quantity;

    @Builder.Default
    private BigDecimal pricePerUnit = BigDecimal.ZERO;
    private String orderId;
}
