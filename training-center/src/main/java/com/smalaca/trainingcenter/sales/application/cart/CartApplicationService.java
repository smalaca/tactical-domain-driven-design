package com.smalaca.trainingcenter.sales.application.cart;

import com.smalaca.annotations.architecture.CommandQueryResponsibilitySegregation;
import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;
import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.cart.CartRepository;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import org.springframework.stereotype.Service;

@DomainDrivenDesign.ApplicationLayer
@PortsAndAdaptersArchitecture.DrivingPort
@CommandQueryResponsibilitySegregation.Command
@Service
public class CartApplicationService {
    private final CartRepository cartRepository;

    CartApplicationService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void addTraining(AddTrainingToCartCommand command) {
        CartId cartId = new CartId(command.cartId());
        TrainingId trainingId = new TrainingId(command.trainingId());
        Cart cart = cartRepository.findBy(cartId);

        cart.add(trainingId);

        cartRepository.save(cart);
    }

    public void removeTraining(RemoveTrainingFromCartCommand command) {
        CartId cartId = new CartId(command.cartId());
        TrainingId trainingId = new TrainingId(command.trainingId());
        Cart cart = cartRepository.findBy(cartId);

        cart.remove(trainingId);

        cartRepository.save(cart);
    }
}
