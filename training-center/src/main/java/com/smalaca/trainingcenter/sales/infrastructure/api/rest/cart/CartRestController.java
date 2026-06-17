package com.smalaca.trainingcenter.sales.infrastructure.api.rest.cart;

import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;
import com.smalaca.trainingcenter.sales.application.cart.AddTrainingToCartCommand;
import com.smalaca.trainingcenter.sales.application.cart.BlockCartCommand;
import com.smalaca.trainingcenter.sales.application.cart.CartApplicationService;
import com.smalaca.trainingcenter.sales.application.cart.RemoveTrainingFromCartCommand;
import com.smalaca.trainingcenter.sales.query.cart.CartQueryService;
import com.smalaca.trainingcenter.sales.query.cart.CartView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
@PortsAndAdaptersArchitecture.DrivingAdapter
class CartRestController {
    private final CartApplicationService applicationService;
    private final CartQueryService queryService;

    CartRestController(CartApplicationService applicationService, CartQueryService queryService) {
        this.applicationService = applicationService;
        this.queryService = queryService;
    }

    @GetMapping
    List<CartView> findAll() {
        return queryService.findAll();
    }

    @GetMapping("/{cartId}")
    ResponseEntity<CartView> findOne(@PathVariable UUID cartId) {
        return queryService.findOneById(cartId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{cartId}/add")
    void add(@PathVariable UUID cartId, @RequestBody CartRequest request) {
        applicationService.addTraining(new AddTrainingToCartCommand(cartId, request.trainingId()));
    }

    @DeleteMapping("/{cartId}/remove")
    void remove(@PathVariable UUID cartId, @RequestBody CartRequest request) {
        applicationService.removeTraining(new RemoveTrainingFromCartCommand(cartId, request.trainingId()));
    }

    @PostMapping("/{cartId}/block")
    void block(@PathVariable UUID cartId) {
        applicationService.block(new BlockCartCommand(cartId));
    }
}
