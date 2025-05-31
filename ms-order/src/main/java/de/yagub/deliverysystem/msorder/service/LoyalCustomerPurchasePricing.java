package de.yagub.deliverysystem.msorder.service;

import de.yagub.deliverysystem.msorder.dto.request.OrderRequest;
import de.yagub.deliverysystem.msorder.dto.response.OrderResponse;
import de.yagub.deliverysystem.msorder.mapper.OrderMapper;
import de.yagub.deliverysystem.msorder.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoyalCustomerPurchasePricing implements PricingStrategy{

    private final StandartPurchasePricing standartPurchasePricing;

    private final OrderMapper orderMapper;

    private final OrderRepository orderRepository;

    @Override
    public BigDecimal calculatePrice(OrderRequest orderRequest) {
        BigDecimal total = standartPurchasePricing.calculatePrice(orderRequest);
        return total.multiply(new BigDecimal("0.75"));
    }

    @Override
    public boolean isSuitable(OrderRequest orderRequest) {
        System.out.println(getClass());
        boolean loyalCustomer = false;
        List<OrderResponse> customerOrders = orderRepository.findByCustomerId(orderRequest.customerId()).stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
        if(customerOrders.size() > 3){
            LocalDateTime loyalCustomerMinimumRequirement = LocalDateTime.now().minusSeconds(15);
            loyalCustomer = customerOrders.stream().anyMatch(e -> e.createdAt().isBefore(loyalCustomerMinimumRequirement));
        }
        return loyalCustomer;
    }

    @Override
    public int filterId() {
        return 1;
    }


}
