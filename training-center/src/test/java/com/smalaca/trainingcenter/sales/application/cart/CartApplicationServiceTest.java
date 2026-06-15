package com.smalaca.trainingcenter.sales.application.cart;

import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartAssertion;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.cart.CartRepository;
import com.smalaca.trainingcenter.sales.domain.cart.CartStatus;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class CartApplicationServiceTest {
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final Clock clock = mock(Clock.class);
    private final OpenTrainingService openTrainingService = mock(OpenTrainingService.class);
    private final CartApplicationService service = new CartApplicationService(cartRepository, clock, openTrainingService);

    @Test
    void shouldAddTrainingToCart() {
        TrainingId trainingId = trainingId();
        CartId cartId = cartId();
        LocalDateTime addedAt = LocalDateTime.of(2026, 6, 15, 21, 30);
        given(clock.now()).willReturn(addedAt);
        given(openTrainingService.hasAlreadyStarted(trainingId)).willReturn(false);
        given(cartRepository.findBy(cartId)).willReturn(Cart.active(cartId));

        service.addTraining(addTrainingTrainingToCartCommand(cartId, trainingId));

        thenSavedCart()
                .hasId(cartId)
                .hasTrainings(1)
                .hasTraining(trainingId, addedAt);
    }

    @Test
    void shouldThrowExceptionWhenAddingTrainingThatIsAlreadyInCart() {
        TrainingId trainingId = trainingId();
        CartId cartId = cartId();
        given(clock.now()).willReturn(LocalDateTime.now());
        given(openTrainingService.hasAlreadyStarted(trainingId)).willReturn(false);
        Cart cart = Cart.active(cartId);
        cart.add(trainingId, clock, openTrainingService);
        given(cartRepository.findBy(cartId)).willReturn(cart);

        Executable executable = () -> service.addTraining(addTrainingTrainingToCartCommand(cartId, trainingId));

        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        assertThat(actual).hasMessage("Training: " + trainingId + " is already in the cart.");
    }

    @Test
    void shouldThrowExceptionWhenAddingTrainingToFullCart() {
        CartId cartId = cartId();
        Cart cart = Cart.active(cartId);
        given(clock.now()).willReturn(LocalDateTime.now());
        given(openTrainingService.hasAlreadyStarted(any())).willReturn(false);
        for (int i = 0; i < 10; i++) {
            cart.add(trainingId(), clock, openTrainingService);
        }
        given(cartRepository.findBy(cartId)).willReturn(cart);

        Executable executable = () -> service.addTraining(addTrainingTrainingToCartCommand(cartId, trainingId()));

        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        assertThat(actual).hasMessage("Cart is full.");
    }

    @Test
    void shouldThrowExceptionWhenAddingTrainingToInactiveCart() {
        CartId cartId = cartId();
        Cart cart = Cart.active(cartId);
        setCartStatus(cart, CartStatus.BLOCKED);
        given(cartRepository.findBy(cartId)).willReturn(cart);

        Executable executable = () -> service.addTraining(addTrainingTrainingToCartCommand(cartId, trainingId()));

        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        assertThat(actual).hasMessage("Cart is not active.");
    }

    @Test
    void shouldThrowExceptionWhenAddingTrainingThatHasAlreadyStarted() {
        CartId cartId = cartId();
        TrainingId trainingId = trainingId();
        given(cartRepository.findBy(cartId)).willReturn(Cart.active(cartId));
        given(openTrainingService.hasAlreadyStarted(trainingId)).willReturn(true);

        Executable executable = () -> service.addTraining(addTrainingTrainingToCartCommand(cartId, trainingId));

        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        assertThat(actual).hasMessage("Training has already started.");
    }

    private void setCartStatus(Cart cart, CartStatus status) {
        try {
            java.lang.reflect.Field field = Cart.class.getDeclaredField("status");
            field.setAccessible(true);
            field.set(cart, status);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private AddTrainingToCartCommand addTrainingTrainingToCartCommand(CartId cartId, TrainingId trainingId) {
        return new AddTrainingToCartCommand(cartId.value(), trainingId.value());
    }

    @Test
    void shouldRemoveTrainingFromCart() {
        CartId cartId = cartId();
        TrainingId trainingId = trainingId();
        given(clock.now()).willReturn(LocalDateTime.now());
        given(openTrainingService.hasAlreadyStarted(trainingId)).willReturn(false);
        Cart cart = Cart.active(cartId);
        cart.add(trainingId, clock, openTrainingService);
        given(cartRepository.findBy(cartId)).willReturn(cart);

        service.removeTraining(removeTrainingTrainingFromCartCommand(cartId, trainingId));

        thenSavedCart()
                .hasId(cartId)
                .hasTrainings(0);
    }

    @Test
    void shouldThrowExceptionWhenRemovingTrainingThatIsNotInCart() {
        CartId cartId = cartId();
        TrainingId trainingId = trainingId();
        given(cartRepository.findBy(cartId)).willReturn(Cart.active(cartId));

        Executable executable = () -> service.removeTraining(removeTrainingTrainingFromCartCommand(cartId, trainingId));

        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        assertThat(actual).hasMessage("Training: " + trainingId + " not found in the cart.");
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
        ArgumentCaptor<Cart> captor = ArgumentCaptor.forClass(Cart.class);
        then(cartRepository).should().save(captor.capture());

        return assertThat(captor.getValue());
    }
}
