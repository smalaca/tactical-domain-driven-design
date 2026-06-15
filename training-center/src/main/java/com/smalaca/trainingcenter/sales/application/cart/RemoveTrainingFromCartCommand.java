package com.smalaca.trainingcenter.sales.application.cart;

import java.util.UUID;

record RemoveTrainingFromCartCommand(UUID cartId, UUID trainingId) {
}
