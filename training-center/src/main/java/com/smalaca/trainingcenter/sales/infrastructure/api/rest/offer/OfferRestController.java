package com.smalaca.trainingcenter.sales.infrastructure.api.rest.offer;

import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;
import com.smalaca.trainingcenter.sales.query.offer.OfferQueryService;
import com.smalaca.trainingcenter.sales.query.offer.OfferView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/offer")
@PortsAndAdaptersArchitecture.DrivingAdapter
class OfferRestController {
    private final OfferQueryService queryService;

    OfferRestController(OfferQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    List<OfferView> findAll() {
        return queryService.findAll();
    }

    @GetMapping("/{offerId}")
    ResponseEntity<OfferView> findOne(@PathVariable UUID offerId) {
        return queryService.findOneById(offerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
