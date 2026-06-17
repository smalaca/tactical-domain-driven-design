package com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.offer;

import com.smalaca.trainingcenter.sales.domain.offer.Offer;
import com.smalaca.trainingcenter.sales.domain.offer.OfferId;

import java.util.Optional;
import java.util.UUID;

public class JpaOfferTestRepository {
    private final JpaOfferRepository jpaOfferRepository;

    public JpaOfferTestRepository(JpaOfferRepository jpaOfferRepository) {
        this.jpaOfferRepository = jpaOfferRepository;
    }

    public Optional<Offer> findById(UUID offerId) {
        return jpaOfferRepository.findById(new OfferId(offerId));
    }

    public void deleteAll() {
        jpaOfferRepository.deleteAll();
    }
}