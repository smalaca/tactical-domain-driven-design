package com.smalaca.trainingcenter.sales.domain.money;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@DomainDrivenDesign.ValueObject
@Embeddable
public record Money(BigDecimal amount) {
}
