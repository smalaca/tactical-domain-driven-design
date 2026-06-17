package com.smalaca.trainingcenter.sales.query.offer;

import com.smalaca.annotations.architecture.CommandQueryResponsibilitySegregation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@CommandQueryResponsibilitySegregation.Query
@Service
public class OfferQueryService {
    private final JpaOfferViewRepository repository;

    OfferQueryService(JpaOfferViewRepository repository) {
        this.repository = repository;
    }

    public Optional<OfferView> findOneById(UUID offerId) {
        return repository.findById(offerId);
    }

    public List<OfferView> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .toList();
    }
}
