package com.smalaca.trainingcenter.sales.infrastructure.clock.local;

import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@PortsAndAdaptersArchitecture.DrivenAdapter
@Component
public class LocalDateTimeClock implements Clock {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
