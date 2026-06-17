package com.smalaca.trainingcenter.sales.infrastructure.api.rest.client;

import java.util.List;
import java.util.UUID;

public record MultipleTrainingTestRequest(List<UUID> trainingIds) {
}
