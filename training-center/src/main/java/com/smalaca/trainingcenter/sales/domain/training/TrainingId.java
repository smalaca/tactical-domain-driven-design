package com.smalaca.trainingcenter.sales.domain.training;

import com.smalaca.annotations.architecture.DomainDrivenDesign;

import java.util.UUID;

@DomainDrivenDesign.ValueObject
public record TrainingId(UUID value) {
}
