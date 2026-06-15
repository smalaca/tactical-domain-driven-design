package com.smalaca.trainingcenter.sales.application.cart;

import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartAssertion;
import com.smalaca.trainingcenter.sales.domain.cart.CartException;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.cart.CartRepository;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.opentrainingservice.OpenTrainingService;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.smalaca.trainingcenter.sales.domain.cart.CartAssertion.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

class CartApplicationServiceTest {
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final Clock clock = mock(Clock.class);
    private final OpenTrainingService openTrainingService = mock(OpenTrainingService.class);
    private final CartApplicationService service = new CartApplicationService(cartRepository, clock, openTrainingService);

    @Test
    void shouldAddTrainingToCart() {
        LocalDateTime addedAt = givenNow(LocalDateTime.of(2026, 6, 15, 21, 30));
        CartId cartId = cartId();
        givenActiveCart(cartId);
        TrainingId trainingId = notStartedTraining();

        service.addTraining(addTrainingTrainingToCartCommand(cartId, trainingId));

        thenSavedCart()
                .hasId(cartId)
                .isActive()
                .hasTrainings(1)
                .hasTraining(trainingId, addedAt);
    }

    @Test
    void shouldAddMultipleTrainingsToCart() {
        CartId cartId = cartId();
        givenActiveCart(cartId);

        LocalDateTime addedAtOne = givenNow(LocalDateTime.of(2026, 6, 15, 21, 30));
        TrainingId trainingIdOne = notStartedTraining();
        service.addTraining(addTrainingTrainingToCartCommand(cartId, trainingIdOne));

        LocalDateTime addedAtTwo = givenNow(LocalDateTime.of(2026, 5, 13, 12, 13));
        TrainingId trainingIdTwo = notStartedTraining();
        service.addTraining(addTrainingTrainingToCartCommand(cartId, trainingIdTwo));

        LocalDateTime addedAtThree = givenNow(LocalDateTime.now());
        TrainingId trainingIdThree = notStartedTraining();
        service.addTraining(addTrainingTrainingToCartCommand(cartId, trainingIdThree));

        thenSavedCart(3)
                .hasId(cartId)
                .hasTrainings(3)
                .hasTraining(trainingIdOne, addedAtOne)
                .hasTraining(trainingIdTwo, addedAtTwo)
                .hasTraining(trainingIdThree, addedAtThree);
    }

    @Test
    void shouldThrowExceptionWhenAddingTrainingThatIsAlreadyInCart() {
        givenNow();
        TrainingId trainingId = notStartedTraining();
        CartId cartId = cartId();
        Cart cart = givenActiveCart(cartId);
        cart.add(trainingId, clock, openTrainingService);

        Executable executable = () -> service.addTraining(addTrainingTrainingToCartCommand(cartId, trainingId));

        CartException actual = assertThrows(CartException.class, executable);
        assertThat(actual).hasMessage("Training: " + trainingId + " is already in the cart.");
    }

    @Test
    void shouldThrowExceptionWhenAddingTrainingToFullCart() {
        givenNow();
        CartId cartId = cartId();
        Cart cart = givenActiveCart(cartId);
        for (int i = 0; i < 10; i++) {
            cart.add(notStartedTraining(), clock, openTrainingService);
        }

        Executable executable = () -> service.addTraining(addTrainingTrainingToCartCommand(cartId, notStartedTraining()));

        CartException actual = assertThrows(CartException.class, executable);
        assertThat(actual).hasMessage("Cart is full.");
    }

    @Test
    void shouldThrowExceptionWhenAddingTrainingToInactiveCart() {
        CartId cartId = cartId();
        givenBlockedCart(cartId);

        Executable executable = () -> service.addTraining(addTrainingTrainingToCartCommand(cartId, notStartedTraining()));

        CartException actual = assertThrows(CartException.class, executable);
        assertThat(actual).hasMessage("Cart is not active.");
    }

    private void givenBlockedCart(CartId cartId) {
        Cart cart = Cart.active(cartId);
        cart.block();
        given(cartRepository.findBy(cartId)).willReturn(cart);
    }

    @Test
    void shouldThrowExceptionWhenAddingTrainingThatHasAlreadyStarted() {
        CartId cartId = cartId();
        givenActiveCart(cartId);
        TrainingId trainingId = startedTraining();

        Executable executable = () -> service.addTraining(addTrainingTrainingToCartCommand(cartId, trainingId));

        CartException actual = assertThrows(CartException.class, executable);
        assertThat(actual).hasMessage("Training: " + trainingId + " has already started.");
    }

    private TrainingId startedTraining() {
        TrainingId trainingId = trainingId();
        given(openTrainingService.hasAlreadyStarted(trainingId)).willReturn(true);
        return trainingId;
    }

    private AddTrainingToCartCommand addTrainingTrainingToCartCommand(CartId cartId, TrainingId trainingId) {
        return new AddTrainingToCartCommand(cartId.value(), trainingId.value());
    }

    @Test
    void shouldRemoveTrainingFromCart() {
        givenNow();
        CartId cartId = cartId();
        TrainingId trainingId = notStartedTraining();
        Cart cart = givenActiveCart(cartId);
        cart.add(trainingId, clock, openTrainingService);

        service.removeTraining(removeTrainingTrainingFromCartCommand(cartId, trainingId));

        thenSavedCart()
                .hasId(cartId)
                .hasTrainings(0);
    }

    @Test
    void shouldThrowExceptionWhenRemovingTrainingThatIsNotInCart() {
        CartId cartId = cartId();
        TrainingId trainingId = trainingId();
        givenActiveCart(cartId);

        Executable executable = () -> service.removeTraining(removeTrainingTrainingFromCartCommand(cartId, trainingId));

        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        assertThat(actual).hasMessage("Training: " + trainingId + " not found in the cart.");
    }

    @Test
    void shouldBlockCart() {
        CartId cartId = cartId();
        givenActiveCart(cartId);

        service.block(new BlockCartCommand(cartId.value()));

        thenSavedCart()
                .hasId(cartId)
                .isBlocked();
    }

    private Cart givenActiveCart(CartId cartId) {
        Cart cart = Cart.active(cartId);
        given(cartRepository.findBy(cartId)).willReturn(cart);
        return cart;
    }

    private void givenNow() {
        givenNow(LocalDateTime.now());
    }

    private TrainingId notStartedTraining() {
        TrainingId trainingId = trainingId();
        given(openTrainingService.hasAlreadyStarted(trainingId)).willReturn(false);
        return trainingId;
    }

    private LocalDateTime givenNow(LocalDateTime addedAt) {
        given(clock.now()).willReturn(addedAt);
        return addedAt;
    }

    private RemoveTrainingFromCartCommand removeTrainingTrainingFromCartCommand(CartId cartId, TrainingId trainingId) {
        return new RemoveTrainingFromCartCommand(cartId.value(), trainingId.value());
    }

    private CartId cartId() {
        return new CartId(id());
    }

    private TrainingId trainingId() {
        return new TrainingId(id());
    }

    private UUID id() {
        return UUID.randomUUID();
    }

    private CartAssertion thenSavedCart() {
        return thenSavedCart(1);
    }

    private CartAssertion thenSavedCart(int wantedNumberOfInvocations) {
        ArgumentCaptor<Cart> captor = ArgumentCaptor.forClass(Cart.class);
        then(cartRepository).should(times(wantedNumberOfInvocations)).save(captor.capture());

        return assertThat(captor.getValue());
    }
}
