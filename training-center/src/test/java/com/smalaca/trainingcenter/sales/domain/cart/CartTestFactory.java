package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.opentrainingservice.OpenTrainingService;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class CartTestFactory {
    private final Clock clock;
    private final OpenTrainingService openTrainingService;

    private CartTestFactory(Clock clock, OpenTrainingService openTrainingService) {
        this.clock = clock;
        this.openTrainingService = openTrainingService;
    }

    public static CartTestFactory cartTestFactory() {
        Clock clock = mock(Clock.class);
        given(clock.now()).willReturn(LocalDateTime.now());
        OpenTrainingService openTrainingService = mock(OpenTrainingService.class);
        given(openTrainingService.hasAlreadyStarted(any())).willReturn(false);

        return new CartTestFactory(clock, openTrainingService);
    }

    public Cart createCart(UUID cartId, List<UUID> trainingIds) {
        Cart cart = Cart.active(new CartId(cartId));
        trainingIds.forEach(id -> cart.add(new TrainingId(id), clock, openTrainingService));

        return cart;
    }
}