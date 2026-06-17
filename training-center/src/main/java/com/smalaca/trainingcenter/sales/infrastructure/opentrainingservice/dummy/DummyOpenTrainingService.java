package com.smalaca.trainingcenter.sales.infrastructure.opentrainingservice.dummy;

import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;
import com.smalaca.trainingcenter.sales.domain.opentrainingservice.OpenTraining;
import com.smalaca.trainingcenter.sales.domain.opentrainingservice.OpenTrainingService;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import org.springframework.stereotype.Service;

import com.smalaca.trainingcenter.sales.domain.opentrainingservice.TrainingStatus;
import com.smalaca.trainingcenter.sales.domain.offer.Money;
import java.math.BigDecimal;
import java.util.Optional;

@PortsAndAdaptersArchitecture.DrivenAdapter
@Service
public class DummyOpenTrainingService implements OpenTrainingService {
    @Override
    public boolean hasAlreadyStarted(TrainingId trainingId) {
        return false;
    }

    @Override
    public Optional<OpenTraining> findBy(TrainingId trainingId) {
        return Optional.of(new OpenTraining(trainingId, TrainingStatus.NOT_STARTED, new Money(BigDecimal.valueOf(100))));
    }
}
