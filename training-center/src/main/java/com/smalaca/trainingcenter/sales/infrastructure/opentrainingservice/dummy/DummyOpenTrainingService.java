package com.smalaca.trainingcenter.sales.infrastructure.opentrainingservice.dummy;

import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;
import com.smalaca.trainingcenter.sales.domain.opentrainingservice.OpenTrainingService;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import org.springframework.stereotype.Service;

@PortsAndAdaptersArchitecture.DrivenAdapter
@Service
public class DummyOpenTrainingService implements OpenTrainingService {
    @Override
    public boolean hasAlreadyStarted(TrainingId trainingId) {
        return false;
    }
}
