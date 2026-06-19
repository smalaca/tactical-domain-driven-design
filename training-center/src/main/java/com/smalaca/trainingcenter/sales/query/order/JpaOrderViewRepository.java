package com.smalaca.trainingcenter.sales.query.order;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface JpaOrderViewRepository extends CrudRepository<OrderView, UUID> {
}
