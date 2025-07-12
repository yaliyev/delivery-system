package de.yagub.deliverysystem.msorder.service;
import de.yagub.deliverysystem.msorder.dto.request.OrderRequest;
import de.yagub.deliverysystem.msorder.model.Promocode;
import de.yagub.deliverysystem.msorder.repository.PromocodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromocodePurchasePricingTest {

    @Mock
    private StandartPurchasePricing standartPurchasePricing;

    @Mock
    private PromocodeRepository promocodeRepository;

    @InjectMocks
    private PromocodePurchasePricing pricingStrategy;

    private OrderRequest orderRequest;
    private Promocode validPromocode;

    @BeforeEach
    void setUp() {
        validPromocode = Promocode.builder()
                .promocode("SUMMER20")
                .amount(new BigDecimal("20.00"))
                .isUsed(false)
                .build();

        orderRequest = new OrderRequest(
                1L,
                List.of(),
                validPromocode
        );
    }

    @Test
    void filterId_shouldReturn2() {
        assertEquals(2, pricingStrategy.filterId());
    }

    @Test
    void calculatePrice_shouldApplyPromocodeDiscount() {
        // Arrange
        BigDecimal standardPrice = new BigDecimal("100.00");
        when(standartPurchasePricing.calculatePrice(orderRequest))
                .thenReturn(standardPrice);
        when(promocodeRepository.findByCode("SUMMER20"))
                .thenReturn(Optional.of(validPromocode));

        // Act
        BigDecimal result = pricingStrategy.calculatePrice(orderRequest);

        // Assert
        assertEquals(new BigDecimal("80.00"), result);
        verify(promocodeRepository).findByCode("SUMMER20");
    }

    @Test
    void isSuitable_shouldReturnTrueForValidUnusedPromocode() {
        // Arrange
        when(promocodeRepository.findByCode("SUMMER20"))
                .thenReturn(Optional.of(validPromocode));

        // Act
        boolean result = pricingStrategy.isSuitable(orderRequest);

        // Assert
        assertTrue(result);
        verify(promocodeRepository).markAsUsed("SUMMER20");
    }

    @Test
    void isSuitable_shouldReturnFalseForUsedPromocode() {
        // Arrange
        Promocode usedPromocode = Promocode.builder()
                .promocode("SUMMER20")
                .isUsed(true)
                .build();

        when(promocodeRepository.findByCode("SUMMER20"))
                .thenReturn(Optional.of(usedPromocode));

        // Act
        boolean result = pricingStrategy.isSuitable(orderRequest);

        // Assert
        assertFalse(result);
        verify(promocodeRepository, never()).markAsUsed(anyString());
    }

    @Test
    void isSuitable_shouldReturnFalseForInvalidPromocode() {
        // Arrange
        when(promocodeRepository.findByCode("SUMMER20"))
                .thenReturn(Optional.empty());

        // Act
        boolean result = pricingStrategy.isSuitable(orderRequest);

        // Assert
        assertFalse(result);
        verify(promocodeRepository, never()).markAsUsed(anyString());
    }

    @Test
    void isSuitable_shouldReturnFalseForNullPromocode() {
        // Arrange
        OrderRequest requestWithoutPromo = new OrderRequest(1L, List.of(), null);

        // Act
        boolean result = pricingStrategy.isSuitable(requestWithoutPromo);

        // Assert
        assertFalse(result);
        verify(promocodeRepository, never()).findByCode(anyString());
    }

    @Test
    void isSuitable_shouldReturnFalseForBlankPromocode() {
        // Arrange
        OrderRequest requestWithBlankPromo = new OrderRequest(
                1L,
                List.of(),
                Promocode.builder().promocode("  ").build()
        );

        // Act
        boolean result = pricingStrategy.isSuitable(requestWithBlankPromo);

        // Assert
        assertFalse(result);
        verify(promocodeRepository, never()).findByCode(anyString());
    }

    @Test
    void calculatePrice_shouldThrowWhenPromocodeNotFound() {
        // Arrange
        when(promocodeRepository.findByCode("SUMMER20"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                pricingStrategy.calculatePrice(orderRequest)
        );

        // Verify standard pricing was never called
        verify(standartPurchasePricing, never()).calculatePrice(any());
    }
}