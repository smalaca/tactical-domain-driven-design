package com.smalaca.trainingcenter.sales.query.cart;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface JpaCartViewRepository extends CrudRepository<CartView, UUID> {
}
