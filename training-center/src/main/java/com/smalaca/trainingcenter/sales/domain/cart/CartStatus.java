package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.annotations.architecture.DomainDrivenDesign;

@DomainDrivenDesign.ValueObject
enum CartStatus {
    ACTIVE, BLOCKED
}
