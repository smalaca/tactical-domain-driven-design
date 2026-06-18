package com.smalaca.trainingcenter.sales.domain.offer.acceptance;

import com.smalaca.trainingcenter.sales.domain.offer.OfferId;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

import java.time.LocalDateTime;
import java.util.Set;

public record OfferAcceptanceParameters(OfferId offerId, LocalDateTime validTo, Set<TrainingId> trainingIds) {
}
