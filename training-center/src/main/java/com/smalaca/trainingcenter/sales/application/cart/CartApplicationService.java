package com.smalaca.trainingcenter.sales.application.cart;

import com.smalaca.annotations.architecture.CommandQueryResponsibilitySegregation;
import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;
import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.cart.CartRepository;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

@DomainDrivenDesign.ApplicationLayer
@PortsAndAdaptersArchitecture.DrivingPort
public class CartApplicationService {
    private final CartRepository cartRepository;

    CartApplicationService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @CommandQueryResponsibilitySegregation.Command
    public void add(AddTrainingToCartCommand command) {
        CartId cartId = new CartId(command.cartId());
        TrainingId trainingId = new TrainingId(command.trainingId());
        Cart cart = cartRepository.findBy(cartId);

        cart.add(trainingId);

        cartRepository.save(cart);
    }

    @CommandQueryResponsibilitySegregation.Command
    public void remove(RemoveTrainingFromCartCommand command) {
        CartId cartId = new CartId(command.cartId());
        TrainingId trainingId = new TrainingId(command.trainingId());
        Cart cart = cartRepository.findBy(cartId);

        cart.remove(trainingId);

        cartRepository.save(cart);
    }
}
