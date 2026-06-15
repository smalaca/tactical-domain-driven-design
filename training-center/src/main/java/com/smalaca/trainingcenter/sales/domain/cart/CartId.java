package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.annotations.architecture.DomainDrivenDesign;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@DomainDrivenDesign.ValueObject
@Embeddable
public record CartId(UUID value) implements Serializable {
}
