package com.smalaca.trainingcenter.sales.infrastructure.api.rest.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderTestDto(UUID orderId, UUID offerId, List<OrderItemTestDto> items, LocalDateTime createdAt) {
    public record OrderItemTestDto(UUID trainingId, BigDecimal price) {}
}
