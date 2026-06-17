package com.smalaca.trainingcenter.sales.query.offer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface JpaOfferViewRepository extends CrudRepository<OfferView, UUID> {
}
