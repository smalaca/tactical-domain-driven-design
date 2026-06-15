package com.smalaca.trainingcenter.sales.domain.opentrainingservice;

import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

@PortsAndAdaptersArchitecture.DrivenPort
public interface OpenTrainingService {
    boolean hasAlreadyStarted(TrainingId trainingId);
}
