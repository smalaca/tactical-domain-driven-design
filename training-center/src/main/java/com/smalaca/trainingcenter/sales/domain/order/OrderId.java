package com.smalaca.trainingcenter.sales.domain.order;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@DomainDrivenDesign.ValueObject
@Embeddable
public record OrderId(UUID value) {
    @DomainDrivenDesign.Factory
    public static OrderId orderId() {
        return new OrderId(UUID.randomUUID());
    }
}
