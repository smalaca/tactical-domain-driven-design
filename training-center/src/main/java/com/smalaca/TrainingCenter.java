package com.smalaca;

import com.smalaca.annotations.architecture.DomainDrivenDesign;

@DomainDrivenDesign.AggregateRoot
public class TrainingCenter {
    public boolean isWorking() {
        return true;
    }
}
