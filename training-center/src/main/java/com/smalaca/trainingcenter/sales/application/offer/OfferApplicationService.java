package com.smalaca.trainingcenter.sales.application.offer;

import com.smalaca.annotations.architecture.CommandQueryResponsibilitySegregation;
import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.offer.Offer;
import com.smalaca.trainingcenter.sales.domain.offer.acceptance.OfferAcceptanceSpecification;
import com.smalaca.trainingcenter.sales.domain.offer.OfferId;
import com.smalaca.trainingcenter.sales.domain.offer.OfferRepository;
import com.smalaca.trainingcenter.sales.domain.order.Order;
import com.smalaca.trainingcenter.sales.domain.order.OrderRepository;
import org.springframework.stereotype.Service;

@DomainDrivenDesign.ApplicationLayer
@PortsAndAdaptersArchitecture.DrivingPort
@CommandQueryResponsibilitySegregation.Command
@Service
public class OfferApplicationService {
    private final OfferRepository offerRepository;
    private final OrderRepository orderRepository;
    private final OfferAcceptanceSpecification specification;
    private final Clock clock;

    OfferApplicationService(OfferRepository offerRepository, OrderRepository orderRepository, OfferAcceptanceSpecification specification, Clock clock) {
        this.offerRepository = offerRepository;
        this.orderRepository = orderRepository;
        this.specification = specification;
        this.clock = clock;
    }

    public void accept(AcceptOfferCommand command) {
        OfferId offerId = new OfferId(command.offerId());
        Offer offer = offerRepository.findBy(offerId);

        Order order = offer.accept(specification, clock);

        orderRepository.save(order);
        offerRepository.save(offer);
    }
}
