package de.yagub.deliverysystem.msorder.dto.request;


import de.yagub.deliverysystem.msorder.model.Promocode;

import java.util.List;

public record OrderRequest (
     Long customerId,
     List<OrderItemRequest> items,
     Promocode promocode
){}
