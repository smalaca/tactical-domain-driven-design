package com.smalaca.trainingcenter.sales.domain.cart;

import com.smalaca.annotations.architecture.DomainDrivenDesign;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.offer.Offer;
import com.smalaca.trainingcenter.sales.domain.opentrainingservice.OpenTraining;
import com.smalaca.trainingcenter.sales.domain.opentrainingservice.OpenTrainingService;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.smalaca.trainingcenter.sales.domain.cart.CartStatus.ACTIVE;
import static com.smalaca.trainingcenter.sales.domain.cart.CartStatus.BLOCKED;

@DomainDrivenDesign.AggregateRoot
@Entity
@Table(name = "CARTS")
public class Cart {
    private static final int MAX_TRAININGS = 10;

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "cart_id"))
    private CartId cartId;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CART_ITEMS", joinColumns = @JoinColumn(name = "cart_id"))
    private Set<CartItem> items = new HashSet<>();

    private Cart() {}

    public static Cart active(CartId cartId) {
        Cart cart = new Cart();
        cart.cartId = cartId;
        cart.status = ACTIVE;
        return cart;
    }

    public void add(TrainingId trainingId, Clock clock, OpenTrainingService openTrainingService) {
        if (isNotActive()) {
            throw CartException.isNotActive();
        }

        if (isFull()) {
            throw CartException.isFull();
        }

        if (openTrainingService.hasAlreadyStarted(trainingId)) {
            throw CartException.trainingAlreadyStarted(trainingId);
        }

        CartItem item = new CartItem(trainingId, clock.now());

        if (items.contains(item)) {
            throw CartException.trainingAlreadyInCart(trainingId);
        }

        items.add(item);
    }

    private boolean isNotActive() {
        return !ACTIVE.equals(status);
    }

    private boolean isFull() {
        return items.size() >= MAX_TRAININGS;
    }

    public void remove(TrainingId trainingId) {
        if (doesNotHave(trainingId)) {
            throw CartException.trainingNotFoundInCart(trainingId);
        }

        items.removeIf(item -> item.isFor(trainingId));
    }

    public void block() {
        status = BLOCKED;
    }

    @DomainDrivenDesign.Factory
    public Offer choose(List<TrainingId> trainings, OpenTrainingService openTrainingService, Clock clock) {
        if (items.isEmpty()) {
            throw CartException.cannotCreateOfferFromEmptyCart(cartId);
        }

        if (isNotActive()) {
            throw CartException.offerCanBeCreatedOnlyForActiveCart(cartId);
        }

        Offer.Builder builder = new Offer.Builder(cartId, clock);
        trainings.forEach(trainingId -> addToOffer(builder, trainingId, openTrainingService));
        return builder.build();
    }

    private void addToOffer(Offer.Builder builder, TrainingId trainingId, OpenTrainingService openTrainingService) {
        if (doesNotHave(trainingId)) {
            throw CartException.cannotChooseTrainingOutsideCart(trainingId);
        }

        OpenTraining openTraining = openTrainingService.findBy(trainingId)
                .orElseThrow(() -> CartException.trainingNotFound(trainingId));

        if (openTraining.hasAlreadyStarted()) {
            throw CartException.trainingAlreadyStarted(trainingId);
        }

        builder.item(openTraining.trainingId(), openTraining.price());
    }

    private boolean doesNotHave(TrainingId trainingId) {
        return items.stream().noneMatch(item -> item.isFor(trainingId));
    }
}
