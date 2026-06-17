package com.smalaca.trainingcenter.sales.infrastructure.api.rest.cart;

import java.util.List;
import java.util.UUID;

public record CartChooseRequest(List<UUID> trainingIds) {
}
