package com.smalaca.trainingcenter.sales.application.cart;

import java.util.UUID;

record AddTrainingToCartCommand(UUID cartId, UUID trainingId) {
}
