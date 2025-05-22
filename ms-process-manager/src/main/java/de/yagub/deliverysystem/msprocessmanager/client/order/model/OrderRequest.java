package de.yagub.deliverysystem.msprocessmanager.client.order.model;

import java.util.List;

public record OrderRequest (
        Long customerId,
        List<OrderItemRequest> items
){}