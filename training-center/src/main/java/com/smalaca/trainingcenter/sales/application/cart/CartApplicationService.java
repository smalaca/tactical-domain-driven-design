package com.smalaca.trainingcenter.sales.application.cart;

import com.smalaca.annotations.architecture.CommandQueryResponsibilitySegregation;
import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;
import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.cart.CartRepository;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.opentrainingservice.OpenTrainingService;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import org.springframework.stereotype.Service;

@DomainDrivenDesign.ApplicationLayer
@PortsAndAdaptersArchitecture.DrivingPort
@CommandQueryResponsibilitySegregation.Command
@Service
public class CartApplicationService {
    private final CartRepository cartRepository;
    private final Clock clock;
    private final OpenTrainingService openTrainingService;

    CartApplicationService(CartRepository cartRepository, Clock clock, OpenTrainingService openTrainingService) {
        this.cartRepository = cartRepository;
        this.clock = clock;
        this.openTrainingService = openTrainingService;
    }

    public void addTraining(AddTrainingToCartCommand command) {
        CartId cartId = new CartId(command.cartId());
        TrainingId trainingId = new TrainingId(command.trainingId());
        Cart cart = cartRepository.findBy(cartId);

        cart.add(trainingId, clock, openTrainingService);

        cartRepository.save(cart);
    }

    public void removeTraining(RemoveTrainingFromCartCommand command) {
        CartId cartId = new CartId(command.cartId());
        TrainingId trainingId = new TrainingId(command.trainingId());
        Cart cart = cartRepository.findBy(cartId);

        cart.remove(trainingId);

        cartRepository.save(cart);
    }

    public void block(BlockCartCommand command) {
        CartId cartId = new CartId(command.cartId());
        Cart cart = cartRepository.findBy(cartId);

        cart.block();

        cartRepository.save(cart);
    }
}
