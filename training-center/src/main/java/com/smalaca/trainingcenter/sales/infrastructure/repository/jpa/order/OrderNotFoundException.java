package com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.order;

import com.smalaca.trainingcenter.sales.domain.order.OrderId;

class OrderNotFoundException extends RuntimeException {
    OrderNotFoundException(OrderId orderId) {
        super("Order with id: " + orderId.value() + " not found.");
    }
}
