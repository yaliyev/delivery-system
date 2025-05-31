package de.yagub.deliverysystem.msorder.mapper;

import de.yagub.deliverysystem.msorder.dto.request.OrderItemRequest;
import de.yagub.deliverysystem.msorder.dto.response.OrderItemResponse;
import de.yagub.deliverysystem.msorder.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItem toOrderItem(OrderItemRequest request);

    OrderItemResponse toOrderItemResponse(OrderItem orderItem);


}
