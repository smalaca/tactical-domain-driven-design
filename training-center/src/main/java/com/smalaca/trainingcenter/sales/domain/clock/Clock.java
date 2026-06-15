package com.smalaca.trainingcenter.sales.domain.clock;

import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;

import java.time.LocalDateTime;

@PortsAndAdaptersArchitecture.DrivenPort
public interface Clock {
    LocalDateTime now();
}
