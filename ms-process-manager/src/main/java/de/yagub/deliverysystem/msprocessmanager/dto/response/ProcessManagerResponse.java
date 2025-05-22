package de.yagub.deliverysystem.msprocessmanager.dto.response;

import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderRequest;
import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderResponse;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.UserResponse;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.WalletResponse;

import java.util.List;

public record ProcessManagerResponse(
        UserResponse userResponse,
        WalletResponse walletResponse,
        List<OrderResponse> orders
) {
}
