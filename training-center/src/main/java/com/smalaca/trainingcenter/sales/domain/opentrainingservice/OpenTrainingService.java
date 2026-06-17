package com.smalaca.trainingcenter.sales.domain.opentrainingservice;

import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import java.util.Optional;

@PortsAndAdaptersArchitecture.DrivenPort
public interface OpenTrainingService {
    boolean hasAlreadyStarted(TrainingId trainingId);

    Optional<OpenTraining> findBy(TrainingId trainingId);
}
