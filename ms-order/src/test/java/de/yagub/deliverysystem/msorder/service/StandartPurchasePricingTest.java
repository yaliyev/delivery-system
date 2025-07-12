package de.yagub.deliverysystem.msorder.service;

import de.yagub.deliverysystem.msorder.dto.request.OrderItemRequest;
import de.yagub.deliverysystem.msorder.dto.request.OrderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StandartPurchasePricingTest {

    private final StandartPurchasePricing pricingStrategy = new StandartPurchasePricing();

    @Test
    void filterId_shouldReturn3() {
        assertEquals(3, pricingStrategy.filterId());
    }

    @Test
    void isSuitable_shouldAlwaysReturnTrue() {
        assertTrue(pricingStrategy.isSuitable(mock(OrderRequest.class)));
        assertTrue(pricingStrategy.isSuitable(null)); // Even with null input
    }

    @Test
    void calculatePrice_shouldReturnZeroForEmptyItems() {
        OrderRequest emptyOrder = new OrderRequest(1L, List.of(), null);
        BigDecimal result = pricingStrategy.calculatePrice(emptyOrder);
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculatePrice_shouldCalculateCorrectTotalForSingleItem() {
        OrderRequest request = new OrderRequest(1L,
                List.of(new OrderItemRequest("item1", 2, new BigDecimal("10.00"))),
                null
        );

        BigDecimal result = pricingStrategy.calculatePrice(request);
        assertEquals(new BigDecimal("20.00"), result);
    }

    @Test
    void calculatePrice_shouldCalculateCorrectTotalForMultipleItems() {
        OrderRequest request = new OrderRequest(1L,
                List.of(
                        new OrderItemRequest("item1", 2, new BigDecimal("10.50")),
                        new OrderItemRequest("item2", 3, new BigDecimal("5.25"))
                ),
                null
        );

        BigDecimal result = pricingStrategy.calculatePrice(request);
        assertEquals(new BigDecimal("36.75"), result);
    }

    @Test
    void calculatePrice_shouldHandleDecimalQuantities() {
        OrderRequest request = new OrderRequest(1L,
                List.of(
                        new OrderItemRequest("item1", 1, new BigDecimal("9.99"))
                ),null);

        BigDecimal result = pricingStrategy.calculatePrice(request);
        assertEquals(new BigDecimal("9.99"), result);
    }

    @Test
    void calculatePrice_shouldNotMutateInputParameters() {
        OrderItemRequest item = new OrderItemRequest("item1", 2, new BigDecimal("10.00"));
        OrderRequest request = new OrderRequest(1L, List.of(item), null);

        pricingStrategy.calculatePrice(request);

        assertEquals(2, item.quantity());
        assertEquals(new BigDecimal("10.00"), item.pricePerUnit());
    }
}
