package de.yagub.deliverysystem.mswallet.service;

import de.yagub.deliverysystem.mswallet.dto.request.CreateWalletRequest;
import de.yagub.deliverysystem.mswallet.dto.request.PaymentRequest;
import de.yagub.deliverysystem.mswallet.dto.response.WalletResponse;
import de.yagub.deliverysystem.mswallet.error.*;
import de.yagub.deliverysystem.mswallet.mapper.WalletMapper;
import de.yagub.deliverysystem.mswallet.model.PaymentType;
import de.yagub.deliverysystem.mswallet.model.Wallet;
import de.yagub.deliverysystem.mswallet.model.WalletStatus;
import de.yagub.deliverysystem.mswallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    private final List<PaymentStrategy> paymentStrategies;

    public WalletResponse createWallet(CreateWalletRequest request) {
        // Check if wallet already exists
        walletRepository.findByUserId(request.userId()).ifPresent(w -> {
            throw new WalletAlreadyExistsException("Wallet already exists for user ID: " + request.userId());
        });

        Wallet wallet = walletMapper.toEntity(request);
        wallet.setCreatedAt(LocalDateTime.now());
        wallet.setUpdatedAt(LocalDateTime.now());
        wallet.setStatus(WalletStatus.ACTIVE);
        wallet.setVersion(0L);

        Wallet savedWallet = walletRepository.create(wallet);
        return walletMapper.toResponse(savedWallet);
    }

    public WalletResponse determinePayment(PaymentRequest paymentRequest){
        PaymentType type;
        try{
             type = PaymentType.valueOf(paymentRequest.paymentType());
        }catch (IllegalArgumentException e){
            throw  new PaymentTypeIsInvalidException(paymentRequest.paymentType() + " type is invalid");
        }
        Optional<WalletResponse> walletResponse = paymentStrategies.stream()
                .filter(strategy -> strategy.getPaymentType().equals(type)).findFirst()
                .map(strategy -> strategy.payment(paymentRequest));
        return walletResponse.get();
    }




    public WalletResponse getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .map(walletMapper::toResponse)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for user ID: " + userId));
    }

    public WalletResponse updateWalletStatus(Long walletId, WalletStatus newStatus) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with ID: " + walletId));

        wallet.setStatus(newStatus);
        wallet.setUpdatedAt(LocalDateTime.now());

        Wallet updatedWallet = walletRepository.update(wallet).get();
        return walletMapper.toResponse(updatedWallet);
    }


    public List<WalletResponse> getAllWallets(int page, int size) {
        int offset = page * size;
        return walletRepository.findAll(size, offset).stream()
                .map(walletMapper::toResponse)
                .collect(Collectors.toList());
    }


    public void deleteWallet(Long walletId) {
        walletRepository.findById(walletId).ifPresent(wallet -> {
            wallet.setStatus(WalletStatus.INACTIVE);
            walletRepository.update(wallet);
        });
    }

}
