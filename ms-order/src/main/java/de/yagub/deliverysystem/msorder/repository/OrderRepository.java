package de.yagub.deliverysystem.msorder.repository;

import de.yagub.deliverysystem.msorder.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(String id);
    List<Order> findByCustomerId(String customerId);
    List<Order> findAll();
}
