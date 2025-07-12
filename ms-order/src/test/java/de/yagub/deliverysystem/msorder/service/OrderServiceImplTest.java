package de.yagub.deliverysystem.msorder.service;

import de.yagub.deliverysystem.msorder.dto.request.OrderItemRequest;
import de.yagub.deliverysystem.msorder.dto.request.OrderRequest;
import de.yagub.deliverysystem.msorder.dto.response.OrderItemResponse;
import de.yagub.deliverysystem.msorder.dto.response.OrderResponse;
import de.yagub.deliverysystem.msorder.error.OrderNotFoundException;
import de.yagub.deliverysystem.msorder.mapper.OrderMapper;
import de.yagub.deliverysystem.msorder.model.*;
import de.yagub.deliverysystem.msorder.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private PricingStrategy standardPricingStrategy;

    @Mock
    private PricingStrategy discountPricingStrategy;

    private OrderServiceImpl orderService;
    private OrderRequest orderRequest;
    private Order order;
    private OrderResponse orderResponse;
    private final String orderId = "order-123";
    private final Long customerId = 1L;
    private final Promocode promocode = Promocode.builder()
            .promocode("SUMMER20")
            .isUsed(false)
            .amount(BigDecimal.valueOf(0.20))
            .build();

    @BeforeEach
    void setUp() {
        initializeTestData();
        orderService = new OrderServiceImpl(
                orderRepository,
                orderMapper,
                List.of(standardPricingStrategy, discountPricingStrategy)
        );
    }

    private void initializeTestData() {
        OrderItem item1 = OrderItem.builder()
                .productId("item-1")
                .quantity(2)
                .pricePerUnit(BigDecimal.valueOf(10.0))
                .build();

        OrderItem item2 = OrderItem.builder()
                .productId("item-2")
                .quantity(1)
                .pricePerUnit(BigDecimal.valueOf(20.0))
                .build();

        orderRequest = new OrderRequest(
                customerId,
                List.of(
                        new OrderItemRequest("item-1", 2, BigDecimal.valueOf(10.0)),
                        new OrderItemRequest("item-2", 1, BigDecimal.valueOf(20.0))
                ),
                promocode
        );

        order = Order.builder()
                .id(orderId)
                .customerId(customerId)
                .items(List.of(item1, item2))
                .totalAmount(BigDecimal.valueOf(40.0))
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<OrderItemResponse> itemResponses = List.of(
                new OrderItemResponse("item-1", 2, BigDecimal.valueOf(10.0)),
                new OrderItemResponse("item-2", 1, BigDecimal.valueOf(20.0))
        );

        orderResponse = new OrderResponse(
                orderId,
                customerId,
                itemResponses,
                BigDecimal.valueOf(40.0),
                OrderStatus.PENDING,
                order.getCreatedAt()
        );
    }

    private void mockStandardStrategy(BigDecimal amount) {
        when(standardPricingStrategy.filterId()).thenReturn(1);
        when(standardPricingStrategy.isSuitable(orderRequest)).thenReturn(true);
        when(standardPricingStrategy.calculatePrice(orderRequest)).thenReturn(amount);
    }

    private void mockDiscountStrategy(BigDecimal amount) {
        when(discountPricingStrategy.filterId()).thenReturn(1);
        when(discountPricingStrategy.isSuitable(orderRequest)).thenReturn(true);
        when(discountPricingStrategy.calculatePrice(orderRequest)).thenReturn(amount);
    }

    private void mockOrderMapping(Order order) {
        when(orderMapper.toOrder(orderRequest)).thenReturn(order);
        when(orderMapper.toOrderResponse(order)).thenReturn(orderResponse);
    }

    private void mockSaveOrder() {
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    private Order createBasicOrder() {
        return Order.builder()
                .status(null)
                .items(Collections.emptyList())
                .build();
    }

    @Test
    void createOrder_shouldSaveWithCorrectState() {
        mockOrderMapping(order);
        mockStandardStrategy(BigDecimal.valueOf(40.0));
        mockSaveOrder();

        orderService.createOrder(orderRequest);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(captor.capture());

        Order savedOrder = captor.getValue();
        assertEquals(OrderStatus.PENDING, savedOrder.getStatus());
        assertEquals(BigDecimal.valueOf(40.0), savedOrder.getTotalAmount());
        assertNotNull(savedOrder.getCreatedAt());
    }

    @Test
    void createOrder_shouldCreateOrderWithItems() {
        mockOrderMapping(order);
        mockStandardStrategy(BigDecimal.valueOf(40.0));
        when(orderRepository.save(order)).thenReturn(order);

        OrderResponse result = orderService.createOrder(orderRequest);

        assertNotNull(result);
        assertEquals(2, result.items().size());
        verify(orderRepository).save(order);
    }

    @Test
    void createOrder_shouldCalculateCorrectTotalAmount() {
        mockOrderMapping(order);
        mockStandardStrategy(BigDecimal.valueOf(40.0));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.createOrder(orderRequest);

        assertEquals(BigDecimal.valueOf(40.0), order.getTotalAmount());
        verify(standardPricingStrategy).calculatePrice(orderRequest);
        verify(discountPricingStrategy, never()).calculatePrice(any());
    }

    @Test
    void getOrderById_shouldReturnOrderWithItems() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toOrderResponse(order)).thenReturn(orderResponse);

        OrderResponse result = orderService.getOrderById(orderId);

        assertNotNull(result);
        assertEquals(2, result.items().size());
        verify(orderRepository).findById(orderId);
    }

    @Test
    void getOrdersByCustomerId_shouldReturnOrdersWithItems() {
        List<Order> orders = List.of(order, order);
        when(orderRepository.findByCustomerId(customerId)).thenReturn(orders);
        when(orderMapper.toOrderResponse(order)).thenReturn(orderResponse);

        List<OrderResponse> results = orderService.getOrdersByCustomerId(customerId);

        assertEquals(2, results.size());
        verify(orderRepository).findByCustomerId(customerId);
    }

    @Test
    void createOrder_shouldSetTimestamps() {
        Order newOrder = createBasicOrder();
        when(orderMapper.toOrder(orderRequest)).thenReturn(newOrder);
        mockStandardStrategy(BigDecimal.TEN);
        mockSaveOrder();

        orderService.createOrder(orderRequest);

        assertNotNull(newOrder.getCreatedAt());
        assertNotNull(newOrder.getUpdatedAt());
        assertEquals(newOrder.getCreatedAt(), newOrder.getUpdatedAt());
    }

    @Test
    void createOrder_shouldSetPendingStatus() {
        Order newOrder = createBasicOrder();
        when(orderMapper.toOrder(orderRequest)).thenReturn(newOrder);
        mockStandardStrategy(BigDecimal.valueOf(100.00));
        mockSaveOrder();
        when(orderMapper.toOrderResponse(any())).thenReturn(OrderResponse.builder().build());

        orderService.createOrder(orderRequest);

        assertEquals(OrderStatus.PENDING, newOrder.getStatus());
    }

    @Test
    void getOrderById_whenNotFound_shouldThrow() {
        when(orderRepository.findById("invalid-id")).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById("invalid-id"));
    }

    @Test
    void getOrdersByCustomerId_whenNoneFound_shouldThrow() {
        when(orderRepository.findByCustomerId(999L)).thenReturn(Collections.emptyList());
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrdersByCustomerId(999L));
    }

    @Test
    void createOrder_withEmptyItems_shouldThrow() {
        OrderRequest emptyRequest = new OrderRequest(1L, Collections.emptyList(), null);

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class,
                () -> orderService.createOrder(emptyRequest));
        assertEquals("Order must contain at least one item", exception.getMessage());
    }

    @Test
    void createOrder_shouldUseFirstApplicableStrategy() {
        when(discountPricingStrategy.filterId()).thenReturn(1);
        when(standardPricingStrategy.filterId()).thenReturn(2);
        when(discountPricingStrategy.isSuitable(orderRequest)).thenReturn(true);
        when(discountPricingStrategy.calculatePrice(orderRequest)).thenReturn(BigDecimal.valueOf(30));
        when(orderMapper.toOrder(orderRequest)).thenReturn(order);
        mockSaveOrder();

        orderService.createOrder(orderRequest);

        verify(discountPricingStrategy).calculatePrice(orderRequest);
        verify(standardPricingStrategy, never()).calculatePrice(any());
    }

    @Test
    void createOrder_whenNoStrategiesSuitable_shouldThrow() {
        Order mockOrder = createBasicOrder();
        when(orderMapper.toOrder(orderRequest)).thenReturn(mockOrder);
        when(standardPricingStrategy.isSuitable(orderRequest)).thenReturn(false);
        when(discountPricingStrategy.isSuitable(orderRequest)).thenReturn(false);

        assertThrows(IllegalStateException.class,
                () -> orderService.createOrder(orderRequest));
    }
}