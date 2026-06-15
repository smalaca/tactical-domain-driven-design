package com.smalaca.trainingcenter.sales.infrastructure.api.rest.cart;

import java.util.List;
import java.util.UUID;

record CartTestDto(UUID cartId, List<UUID> trainingIds) {
}
