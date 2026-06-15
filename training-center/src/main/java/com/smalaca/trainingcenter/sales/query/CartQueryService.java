package com.smalaca.trainingcenter.sales.query;

import com.smalaca.annotations.architecture.CommandQueryResponsibilitySegregation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@CommandQueryResponsibilitySegregation.Query
@Service
public class CartQueryService {
    private final JpaCartViewRepository repository;

    CartQueryService(JpaCartViewRepository repository) {
        this.repository = repository;
    }

    public Optional<CartView> findOneById(UUID cartId) {
        return repository.findById(cartId);
    }

    public List<CartView> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .toList();
    }
}
