package com.smalaca;

import com.smalaca.annotations.architecture.DomainDrivenDesign;

@DomainDrivenDesign.AggregateRoot
class TrainingCenter {
    boolean isWorking() {
        return true;
    }
}
