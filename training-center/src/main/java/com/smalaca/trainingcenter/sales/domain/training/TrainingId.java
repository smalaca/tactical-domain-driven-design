package com.smalaca.trainingcenter.sales.domain.training;

import com.smalaca.annotations.architecture.DomainDrivenDesign;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@DomainDrivenDesign.ValueObject
@Embeddable
public record TrainingId(UUID value) implements Serializable {
}
