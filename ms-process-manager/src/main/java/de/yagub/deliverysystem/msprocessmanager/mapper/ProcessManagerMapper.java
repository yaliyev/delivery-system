package de.yagub.deliverysystem.msprocessmanager.mapper;

import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderRequest;
import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderResponse;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.UserResponse;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.CreateWalletRequest;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.WalletResponse;
import de.yagub.deliverysystem.msprocessmanager.dto.response.ProcessManagerResponse;
import de.yagub.deliverysystem.msprocessmanager.service.ProcessManagerService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProcessManagerMapper {




    @Mapping(target = "userId",source = "userResponse.id")
    public CreateWalletRequest mergeIntoCreateWalletRequest(CreateWalletRequest createWalletRequest,UserResponse userResponse);

    @Mapping(target = "customerId",source = "userResponse.id")
    public OrderRequest mergeIntoOrderRequest(OrderRequest orderRequest, UserResponse userResponse);

    @Mapping(target = "walletStatus",source = "walletResponse.status")
    @Mapping(target = "walletCreatedAt",source = "walletResponse.createdAt")
    @Mapping(target = "orderStatus",source = "orderResponse.status")
    @Mapping(target = "orderCreatedAt",source = "orderResponse.createdAt")
    public ProcessManagerResponse mergeIntoProcessManagerResponse(UserResponse userResponse, WalletResponse walletResponse, OrderResponse orderResponse);
}
