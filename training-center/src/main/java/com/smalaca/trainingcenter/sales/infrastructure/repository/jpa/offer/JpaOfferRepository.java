package com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.offer;

import com.smalaca.trainingcenter.sales.domain.offer.Offer;
import com.smalaca.trainingcenter.sales.domain.offer.OfferId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface JpaOfferRepository extends CrudRepository<Offer, OfferId> {
}
