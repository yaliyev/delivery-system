package de.yagub.deliverysystem.msprocessmanager.dto.request;

import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderRequest;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.RegistrationRequest;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.CreateWalletRequest;

public record ProcessManagerRequest(
       RegistrationRequest registrationRequest,
       CreateWalletRequest createWalletRequest,
       OrderRequest orderRequest
)
{}
