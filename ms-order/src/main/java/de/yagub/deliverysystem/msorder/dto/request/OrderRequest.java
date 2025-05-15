package de.yagub.deliverysystem.msorder.dto.request;


import java.util.List;

public record OrderRequest (
     String customerId,
     List<OrderItemRequest> items
){}
