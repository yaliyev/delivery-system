package de.yagub.deliverysystem.msorder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String productId;
    private int quantity;
    private BigDecimal pricePerUnit = BigDecimal.ZERO;
    private String orderId;
}
