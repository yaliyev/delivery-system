package de.yagub.deliverysystem.msorder.dto.response;

public record PromocodeResponse(
        String promocode,
        Boolean isUsed
) {
}
