package de.yagub.deliverysystem.mswallet.repository;

import de.yagub.deliverysystem.mswallet.model.Wallet;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface WalletRepository {
    Wallet create(Wallet wallet);

    Optional<Wallet> findById(Long id);
    Optional<Wallet> findByUserId(Long userId);

    int updateBalance(Long walletId, BigDecimal amount);
    List<Wallet> findAll(int limit, int offset);
    Optional<Wallet> update(Wallet wallet);

    boolean transferFunds(Long fromWalletId, Long toWalletId, BigDecimal amount,Long fromWalletVersion,Long toWalletVersion);

}
