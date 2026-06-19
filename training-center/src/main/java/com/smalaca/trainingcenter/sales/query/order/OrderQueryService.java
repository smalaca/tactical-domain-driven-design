package com.smalaca.trainingcenter.sales.query.order;

import com.smalaca.annotations.architecture.CommandQueryResponsibilitySegregation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@CommandQueryResponsibilitySegregation.Query
@Service
public class OrderQueryService {
    private final JpaOrderViewRepository repository;

    OrderQueryService(JpaOrderViewRepository repository) {
        this.repository = repository;
    }

    public Optional<OrderView> findOneById(UUID orderId) {
        return repository.findById(orderId);
    }

    public List<OrderView> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .toList();
    }
}
