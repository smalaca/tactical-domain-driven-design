package com.smalaca.trainingcenter.sales.application.cart;

import java.util.List;
import java.util.UUID;

public record ChooseTrainingsCommand(UUID cartId, List<UUID> trainingIds) {
}
