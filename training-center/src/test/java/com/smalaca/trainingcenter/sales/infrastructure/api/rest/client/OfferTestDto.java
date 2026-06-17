package com.smalaca.trainingcenter.sales.infrastructure.api.rest.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OfferTestDto(UUID offerId, UUID cartId, List<OfferItemTestDto> items, LocalDateTime createdAt, LocalDateTime validTo) {
    public record OfferItemTestDto(UUID trainingId, BigDecimal price) {}
}
