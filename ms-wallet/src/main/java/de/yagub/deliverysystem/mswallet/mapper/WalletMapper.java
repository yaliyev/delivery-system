package de.yagub.deliverysystem.mswallet.mapper;

import de.yagub.deliverysystem.mswallet.dto.request.CreateWalletRequest;
import de.yagub.deliverysystem.mswallet.dto.response.WalletResponse;
import de.yagub.deliverysystem.mswallet.model.Wallet;
import de.yagub.deliverysystem.mswallet.model.WalletStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    @Mapping(target = "balance",source = "initialBalance")
    Wallet toEntity(CreateWalletRequest dto);

    @Mapping(target = "status", qualifiedByName = "statusToString")
    WalletResponse toResponse(Wallet entity);

    @Named("stringToStatus")
    default WalletStatus stringToStatus(String status) {
        return status != null ? WalletStatus.valueOf(status) : WalletStatus.ACTIVE;
    }

    @Named("statusToString")
    default String statusToString(WalletStatus status) {
        return status != null ? status.name() : null;
    }
}
