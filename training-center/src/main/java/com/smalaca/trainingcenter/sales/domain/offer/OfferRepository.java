package com.smalaca.trainingcenter.sales.domain.offer;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;

@DomainDrivenDesign.Repository
@PortsAndAdaptersArchitecture.DrivenPort
public interface OfferRepository {
    Offer findBy(OfferId offerId);

    void save(Offer offer);
}
