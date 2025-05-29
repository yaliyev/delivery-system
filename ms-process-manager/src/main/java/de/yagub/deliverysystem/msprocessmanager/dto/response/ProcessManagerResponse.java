package de.yagub.deliverysystem.msprocessmanager.dto.response;

import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderItemResponse;
import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderRequest;
import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderResponse;
import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderStatus;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.UserResponse;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.WalletResponse;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.WalletStatus;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProcessManagerResponse(
        String username,
        boolean enabled,
        BigDecimal balance,
        String currency,
        WalletStatus walletStatus,
        LocalDateTime walletCreatedAt,
        List<OrderItemResponse> items,
        BigDecimal totalAmount,
        OrderStatus orderStatus,
        LocalDateTime orderCreatedAt
) {
}
