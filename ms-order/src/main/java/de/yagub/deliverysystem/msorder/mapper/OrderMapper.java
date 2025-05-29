package de.yagub.deliverysystem.msorder.mapper;

import de.yagub.deliverysystem.msorder.dto.request.OrderRequest;
import de.yagub.deliverysystem.msorder.dto.response.OrderResponse;
import de.yagub.deliverysystem.msorder.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = OrderItemMapper.class)
public interface OrderMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toOrder(OrderRequest request);



    OrderResponse toOrderResponse(Order order);

}
