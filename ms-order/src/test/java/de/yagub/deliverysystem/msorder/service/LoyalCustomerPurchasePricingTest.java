package de.yagub.deliverysystem.msorder.service;

import de.yagub.deliverysystem.msorder.dto.request.OrderRequest;
import de.yagub.deliverysystem.msorder.dto.response.OrderResponse;
import de.yagub.deliverysystem.msorder.mapper.OrderMapper;
import de.yagub.deliverysystem.msorder.model.Order;
import de.yagub.deliverysystem.msorder.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoyalCustomerPurchasePricingTest {

    @Mock
    private StandartPurchasePricing standartPurchasePricing;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private LoyalCustomerPurchasePricing pricingStrategy;

    private final Long customerId = 1L;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        orderRequest = new OrderRequest(customerId, List.of(), null);
    }


    @Test
    void isSuitable_shouldReturnTrueWhenCustomerHasOldOrders() {
        // Arrange
        LocalDateTime oldTime = LocalDateTime.now().minusMinutes(30);
        Order oldOrder = Order.builder()
                .id("order-1")
                .createdAt(oldTime)
                .build();

        when(orderRepository.findByCustomerId(customerId))
                .thenReturn(List.of(
                        createOrder(2),
                        createOrder(3),
                        createOrder(4),
                        oldOrder
                ));

        // More flexible mocking
        when(orderMapper.toOrderResponse(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order order = invocation.getArgument(0);
                    return OrderResponse.builder()
                            .createdAt(order.getCreatedAt())
                            .build();
                });

        // Act & Assert
        assertTrue(pricingStrategy.isSuitable(orderRequest));
    }

    @Test
    void calculatePrice_shouldApply25PercentDiscount() {
        // Arrange
        BigDecimal standardPrice = new BigDecimal("100.00");
        when(standartPurchasePricing.calculatePrice(orderRequest))
                .thenReturn(standardPrice);

        // Act
        BigDecimal result = pricingStrategy.calculatePrice(orderRequest);

        // Assert
        assertEquals(new BigDecimal("75.0000"), result);
    }

    private Order createOrder(int id) {
        return Order.builder()
                .id("order-" + id)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
