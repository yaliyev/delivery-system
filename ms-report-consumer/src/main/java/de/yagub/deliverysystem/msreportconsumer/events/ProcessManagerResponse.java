package de.yagub.deliverysystem.msreportconsumer.events;

import de.yagub.deliverysystem.msreportconsumer.model.OrderItemResponse;
import de.yagub.deliverysystem.msreportconsumer.model.OrderStatus;
import de.yagub.deliverysystem.msreportconsumer.model.WalletStatus;
import lombok.Data;

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
