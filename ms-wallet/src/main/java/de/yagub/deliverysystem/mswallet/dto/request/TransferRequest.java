package de.yagub.deliverysystem.mswallet.dto.request;

import java.math.BigDecimal;

public record TransferRequest (
    Long fromUserId,
    Long toUserId,
    BigDecimal amount

){}
