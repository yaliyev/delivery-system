package de.yagub.deliverysystem.msorder.dto.request;

public record OrderItemRequest(
     String productId,
     int quantity
) { }
