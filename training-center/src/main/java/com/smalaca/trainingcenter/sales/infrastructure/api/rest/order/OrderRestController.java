package com.smalaca.trainingcenter.sales.infrastructure.api.rest.order;

import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;
import com.smalaca.trainingcenter.sales.query.order.OrderQueryService;
import com.smalaca.trainingcenter.sales.query.order.OrderView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
@PortsAndAdaptersArchitecture.DrivingAdapter
class OrderRestController {
    private final OrderQueryService queryService;

    OrderRestController(OrderQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    List<OrderView> findAll() {
        return queryService.findAll();
    }

    @GetMapping("/{orderId}")
    ResponseEntity<OrderView> findOne(@PathVariable UUID orderId) {
        return queryService.findOneById(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
