package com.smalaca.trainingcenter.sales.application.cart;

import java.util.UUID;

public record AddTrainingToCartCommand(UUID cartId, UUID trainingId) {
}
