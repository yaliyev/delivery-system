package de.yagub.deliverysystem.msprocessmanager.client.order.model;

public record OrderItemRequest(
        String productId,
        int quantity
) { }
