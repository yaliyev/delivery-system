package de.yagub.deliverysystem.mswallet.service;


import de.yagub.deliverysystem.mswallet.dto.request.CreateWalletRequest;
import de.yagub.deliverysystem.mswallet.dto.request.PaymentRequest;
import de.yagub.deliverysystem.mswallet.dto.response.WalletResponse;
import de.yagub.deliverysystem.mswallet.error.*;
import de.yagub.deliverysystem.mswallet.mapper.WalletMapper;
import de.yagub.deliverysystem.mswallet.model.*;
import de.yagub.deliverysystem.mswallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;
    @Mock
    private WalletMapper walletMapper;
    @Mock
    private PaymentStrategy creditCardStrategy;
    @Mock
    private PaymentStrategy balanceStrategy;

    @Mock
    private PaymentStrategy transferStrategy;


    @InjectMocks
    private WalletService walletService;

    private final Long userId = 1L;
    private final Long walletId = 1L;
    private Wallet wallet;
    private WalletResponse walletResponse;


    @BeforeEach
    void setUp() {
        wallet = Wallet.builder()
                .id(walletId)
                .userId(userId)
                .balance(new BigDecimal("100.00"))
                .status(WalletStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(0L)
                .build();

        walletResponse = WalletResponse.builder()
                .id(walletId)
                .userId(userId)
                .balance(new BigDecimal("100.00"))
                .status(WalletStatus.ACTIVE)
                .build();


        // Pre-configure strategy types (good practice)


        walletService = new WalletService(
                walletRepository,
                walletMapper,
                List.of(creditCardStrategy, balanceStrategy,transferStrategy)
        );

        // Use lenient stubbing for all strategy methods
        lenient().when(creditCardStrategy.getPaymentType()).thenReturn(PaymentType.CREDIT_CARD);
        lenient().when(creditCardStrategy.payment(any())).thenReturn(walletResponse);

        lenient().when(balanceStrategy.getPaymentType()).thenReturn(PaymentType.BALANCE);
        lenient().when(balanceStrategy.payment(any())).thenReturn(walletResponse);

        lenient().when(transferStrategy.getPaymentType()).thenReturn(PaymentType.TRANSFER);
        lenient().when(transferStrategy.payment(any())).thenReturn(walletResponse);


    }

    @Test
    void createWallet_shouldCreateNewWallet() {
        CreateWalletRequest request = new CreateWalletRequest(userId, new BigDecimal("100.00"),"USD");
        when(walletRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(walletMapper.toEntity(request)).thenReturn(wallet);
        when(walletRepository.create(any(Wallet.class))).thenReturn(wallet);
        when(walletMapper.toResponse(wallet)).thenReturn(walletResponse);

        WalletResponse result = walletService.createWallet(request);

        assertNotNull(result);
        assertEquals(walletId, result.id());
        verify(walletRepository).create(any(Wallet.class));
    }

    @Test
    void createWallet_shouldValidateCurrency() {
        CreateWalletRequest request = new CreateWalletRequest(userId, new BigDecimal("100.00"), null);

        // The mapper will throw NPE when currency is null
        when(walletMapper.toEntity(request)).thenThrow(new NullPointerException("currency"));

        assertThrows(InvalidPaymentRequestException.class, () ->
                walletService.createWallet(request)
        );
    }

    @Test
    void determinePayment_shouldThrowWhenStrategyNotFound() {
        // Create service without any strategies
        WalletService service = new WalletService(walletRepository, walletMapper, List.of());

        PaymentRequest request = new PaymentRequest(
                walletId,
                new BigDecimal("50.00"),
                "CREDIT_CARD",  // Valid type but no strategy available
                null
        );

        assertThrows(PaymentStrategyNotFoundException.class, () ->
                service.determinePayment(request)
        );
    }

    @Test
    void determinePayment_shouldCorrectlySelectBalanceStrategy() {
        PaymentRequest request = new PaymentRequest(
                walletId,
                new BigDecimal("50.00"),
                "BALANCE",
                null
        );

        walletService.determinePayment(request);

        verify(balanceStrategy).payment(request);
        verify(creditCardStrategy, never()).payment(any());
        verify(transferStrategy, never()).payment(any());
    }

    @Test
    void determinePayment_shouldValidateTransferReceiver() {
        PaymentRequest request = new PaymentRequest(
                walletId,
                new BigDecimal("50.00"),
                "TRANSFER",
                999L // Non-existing receiver
        );

        when(transferStrategy.payment(request))
                .thenThrow(new WalletNotFoundException("Receiver wallet not found"));

        assertThrows(WalletNotFoundException.class, () ->
                walletService.determinePayment(request)
        );
    }

    @Test
    void deleteWallet_shouldSetInactiveStatusAndUpdateTimestamp() {
        LocalDateTime initialUpdateTime = wallet.getUpdatedAt();
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.deleteWallet(walletId);

        assertEquals(WalletStatus.INACTIVE, wallet.getStatus());
        assertTrue(wallet.getUpdatedAt().isAfter(initialUpdateTime));
        verify(walletRepository).update(wallet);
    }

    @Test
    void updateWalletStatus_shouldUpdateTimestamp() {
        WalletStatus newStatus = WalletStatus.SUSPENDED;
        LocalDateTime initialUpdateTime = wallet.getUpdatedAt();

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.update(wallet)).thenReturn(Optional.of(wallet));

        walletService.updateWalletStatus(walletId, newStatus);

        assertTrue(wallet.getUpdatedAt().isAfter(initialUpdateTime));
        verify(walletRepository).update(wallet);
    }

    @Test
    void getAllWallets_shouldHandlePageOverflow() {
        int page = 100;
        int size = 10;

        when(walletRepository.findAll(size, page * size))
                .thenReturn(List.of());

        List<WalletResponse> results = walletService.getAllWallets(page, size);

        assertTrue(results.isEmpty());
        verify(walletRepository).findAll(size, 1000);
    }

    @Test
    void createWallet_shouldAllowZeroBalance() {
        CreateWalletRequest request = new CreateWalletRequest(userId, BigDecimal.ZERO, "USD");

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(walletMapper.toEntity(request)).thenReturn(wallet);
        when(walletRepository.create(any(Wallet.class))).thenReturn(wallet);

        assertDoesNotThrow(() -> walletService.createWallet(request));
    }

    @Test
    void determinePayment_shouldRejectNegativeAmounts() {
        PaymentRequest request = new PaymentRequest(
                walletId,
                new BigDecimal("-50.00"),
                "BALANCE",
                null
        );

        assertThrows(InvalidPaymentRequestException.class, () ->
                walletService.determinePayment(request)
        );
    }

    @Test
    void determinePayment_shouldRejectZeroAmount() {
        PaymentRequest request = new PaymentRequest(
                walletId,
                BigDecimal.ZERO,  // Zero amount
                "BALANCE",
                null
        );

        assertThrows(InvalidPaymentRequestException.class, () ->
                walletService.determinePayment(request)
        );
    }

    @Test
    void determinePayment_shouldAcceptPositiveAmount() {
        PaymentRequest request = new PaymentRequest(
                walletId,
                new BigDecimal("50.00"),  // Positive amount
                "BALANCE",
                null
        );

        when(balanceStrategy.getPaymentType()).thenReturn(PaymentType.BALANCE);
        when(balanceStrategy.payment(request)).thenReturn(walletResponse);

        assertDoesNotThrow(() -> walletService.determinePayment(request));
    }

    @Test
    void determinePayment_shouldPreventSelfTransfers() {
        PaymentRequest request = new PaymentRequest(
                walletId,
                new BigDecimal("50.00"),
                "TRANSFER",
                walletId  // Sending to self
        );

        when(transferStrategy.payment(request))
                .thenThrow(new InvalidPaymentRequestException("Cannot transfer to self"));

        assertThrows(InvalidPaymentRequestException.class, () ->
                walletService.determinePayment(request)
        );
    }

    @Test
    void createWallet_shouldThrowWhenWalletExists() {
        CreateWalletRequest request = new CreateWalletRequest(userId, new BigDecimal("100.00"),"USD");
        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(wallet));

        assertThrows(WalletAlreadyExistsException.class, () ->
                walletService.createWallet(request)
        );
    }

    @Test
    void getWalletByUserId_shouldReturnWallet() {
        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(wallet));
        when(walletMapper.toResponse(wallet)).thenReturn(walletResponse);

        WalletResponse result = walletService.getWalletByUserId(userId);

        assertNotNull(result);
        assertEquals(userId, result.userId());
    }

    @Test
    void getWalletByUserId_shouldThrowWhenNotFound() {
        when(walletRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () ->
                walletService.getWalletByUserId(userId)
        );
    }

    @Test
    void updateWalletStatus_shouldUpdateStatus() {
        WalletStatus newStatus = WalletStatus.SUSPENDED;
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.update(wallet)).thenReturn(Optional.of(wallet));
        when(walletMapper.toResponse(wallet)).thenReturn(walletResponse);

        WalletResponse result = walletService.updateWalletStatus(walletId, newStatus);

        assertEquals(newStatus, wallet.getStatus());
        assertNotNull(result);
    }

    @Test
    void determinePayment_shouldUseCorrectStrategy() {
        // Correct argument order: walletId, amount, paymentType, receiverWalletId
        PaymentRequest request = new PaymentRequest(
                walletId,
                new BigDecimal("50.00"),
                "CREDIT_CARD",
                null // receiverWalletId can be null for credit card payments
        );

        when(creditCardStrategy.getPaymentType()).thenReturn(PaymentType.CREDIT_CARD);
        when(creditCardStrategy.payment(request)).thenReturn(walletResponse);

        WalletResponse result = walletService.determinePayment(request);

        assertNotNull(result);
        verify(creditCardStrategy).payment(request);
    }

    @Test
    void determinePayment_shouldThrowWhenPaymentTypeInvalid() {
        PaymentRequest request = new PaymentRequest(
                walletId,
                new BigDecimal("50.00"),
                "INVALID_TYPE",  // Invalid payment type
                null
        );

        assertThrows(PaymentTypeIsInvalidException.class, () ->
                walletService.determinePayment(request)
        );
    }

    @Test
    void determinePayment_shouldUseTransferStrategy() {
        // Arrange
        Long receiverWalletId = 2L;
        PaymentRequest request = new PaymentRequest(
                walletId,
                new BigDecimal("50.00"),
                "TRANSFER",
                receiverWalletId
        );

        // Mock transfer strategy
        when(transferStrategy.payment(request)).thenReturn(walletResponse);

        // Act
        WalletResponse result = walletService.determinePayment(request);

        // Assert
        assertNotNull(result);
        verify(transferStrategy).payment(request);
        verify(creditCardStrategy, never()).payment(any());
        verify(balanceStrategy, never()).payment(any());
    }

    @Test
    void determinePayment_shouldRequireReceiverForTransfers() {
        PaymentRequest request = new PaymentRequest(
                walletId,
                new BigDecimal("50.00"),
                "TRANSFER",
                null // Missing receiver
        );

        assertThrows(InvalidPaymentRequestException.class, () ->
                walletService.determinePayment(request)
        );
    }

    @Test
    void determinePayment_shouldHandleAllStrategyTypes() {
        // Setup mocks for each strategy


        // Test all payment types
        List.of(
                new PaymentRequest(walletId, new BigDecimal("50.00"), "CREDIT_CARD", null),
                new PaymentRequest(walletId, new BigDecimal("50.00"), "BALANCE", 2L),
                new PaymentRequest(walletId, new BigDecimal("50.00"), "TRANSFER", 2L)
        ).forEach(request -> {
            // Act & Assert
            WalletResponse result = assertDoesNotThrow(() ->
                    walletService.determinePayment(request)
            );
            assertNotNull(result);
        });
    }

    @Test
    void determinePayment_shouldUseBalanceStrategyForBalanceTransfers() {
        Long receiverWalletId = 2L;
        PaymentRequest request = new PaymentRequest(
                walletId,
                new BigDecimal("50.00"),
                "BALANCE",
                receiverWalletId
        );

        // REMOVED redundant stubs:
        // when(balanceStrategy.getPaymentType()).thenReturn(PaymentType.BALANCE);
        // when(creditCardStrategy.getPaymentType()).thenReturn(PaymentType.CREDIT_CARD);

        when(balanceStrategy.payment(request)).thenReturn(walletResponse);

        WalletResponse result = walletService.determinePayment(request);

        assertNotNull(result);
        verify(balanceStrategy).payment(request);
    }

    @Test
    void determinePayment_shouldNotRequireReceiverForBalancePayments() {
        PaymentRequest request = new PaymentRequest(
                walletId,
                new BigDecimal("50.00"),
                "BALANCE",
                null // No receiver needed for balance payments
        );

        when(balanceStrategy.payment(request)).thenReturn(walletResponse);

        // Should NOT throw exception
        WalletResponse result = walletService.determinePayment(request);
        assertNotNull(result);
    }


    @Test
    void getAllWallets_shouldReturnPaginatedResults() {
        int page = 0;
        int size = 10;
        when(walletRepository.findAll(size, page * size))
                .thenReturn(List.of(wallet));
        when(walletMapper.toResponse(wallet)).thenReturn(walletResponse);

        List<WalletResponse> results = walletService.getAllWallets(page, size);

        assertEquals(1, results.size());
        verify(walletRepository).findAll(size, 0);
    }

    @Test
    void deleteWallet_shouldSetStatusToInactive() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.deleteWallet(walletId);

        assertEquals(WalletStatus.INACTIVE, wallet.getStatus());
        verify(walletRepository).update(wallet);
    }
}
