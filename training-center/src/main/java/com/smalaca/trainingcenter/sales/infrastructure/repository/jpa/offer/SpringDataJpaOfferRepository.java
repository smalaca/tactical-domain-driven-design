package com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.offer;

import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;
import com.smalaca.trainingcenter.sales.domain.offer.Offer;
import com.smalaca.trainingcenter.sales.domain.offer.OfferId;
import com.smalaca.trainingcenter.sales.domain.offer.OfferRepository;
import org.springframework.stereotype.Repository;

@PortsAndAdaptersArchitecture.DrivenAdapter
@Repository
public class SpringDataJpaOfferRepository implements OfferRepository {
    private final JpaOfferRepository jpaOfferRepository;

    public SpringDataJpaOfferRepository(JpaOfferRepository jpaOfferRepository) {
        this.jpaOfferRepository = jpaOfferRepository;
    }

    @Override
    public Offer findBy(OfferId offerId) {
        return jpaOfferRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException(offerId));
    }

    @Override
    public void save(Offer offer) {
        jpaOfferRepository.save(offer);
    }
}
