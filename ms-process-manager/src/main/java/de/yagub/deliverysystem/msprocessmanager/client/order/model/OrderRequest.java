package de.yagub.deliverysystem.msprocessmanager.client.order.model;

import java.util.List;

public record OrderRequest (
        String customerId,
        List<OrderItemRequest> items
){}