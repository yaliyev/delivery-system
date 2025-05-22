package de.yagub.deliverysystem.msprocessmanager.mapper;

import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderRequest;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.UserResponse;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.CreateWalletRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProcessManagerMapper {




    @Mapping(target = "userId",source = "userResponse.id")
    public CreateWalletRequest mergeIntoCreateWalletRequest(CreateWalletRequest createWalletRequest,UserResponse userResponse);

    @Mapping(target = "customerId",source = "userResponse.id")
    public OrderRequest mergeIntoOrderRequest(OrderRequest orderRequest, UserResponse userResponse);


}
