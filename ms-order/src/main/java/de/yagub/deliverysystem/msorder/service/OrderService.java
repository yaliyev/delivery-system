package de.yagub.deliverysystem.msorder.service;

import de.yagub.deliverysystem.msorder.dto.request.OrderRequest;
import de.yagub.deliverysystem.msorder.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);


    OrderResponse getOrderById(String orderId);

    List<OrderResponse> getOrdersByCustomerId(Long customerId);


    List<OrderResponse> getAllOrders();
}