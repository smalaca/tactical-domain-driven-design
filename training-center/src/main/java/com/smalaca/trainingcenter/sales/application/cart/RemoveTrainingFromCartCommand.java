package com.smalaca.trainingcenter.sales.application.cart;

import java.util.UUID;

public record RemoveTrainingFromCartCommand(UUID cartId, UUID trainingId) {
}
