package com.smalaca.trainingcenter.sales.query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface JpaCartViewRepository extends CrudRepository<CartView, UUID> {
}
