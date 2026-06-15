package com.smalaca.trainingcenter.sales.infrastructure.api.rest.client;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CartTestDto(UUID cartId, List<CartItemTestDto> items) {
    public record CartItemTestDto(UUID trainingId, LocalDateTime addedAt) {}
}
