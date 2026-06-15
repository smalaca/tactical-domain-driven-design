package com.smalaca.trainingcenter.sales.application.cart;

import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartAssertion;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.cart.CartRepository;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static com.smalaca.trainingcenter.sales.domain.cart.CartAssertion.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class CartApplicationServiceTest {
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final CartApplicationService service = new CartApplicationService(cartRepository);

    @Test
    void shouldAddTrainingToCart() {
        TrainingId trainingId = trainingId();
        CartId cartId = cartId();
        given(cartRepository.findBy(cartId)).willReturn(new Cart(cartId));

        service.addTraining(addTrainingTrainingToCartCommand(cartId, trainingId));

        thenSavedCart()
                .hasId(cartId)
                .hasTrainings(1)
                .hasTraining(trainingId);
    }

    @Test
    void shouldThrowExceptionWhenAddingTrainingThatIsAlreadyInCart() {
        TrainingId trainingId = trainingId();
        CartId cartId = cartId();
        Cart cart = new Cart(cartId);
        cart.add(trainingId);
        given(cartRepository.findBy(cartId)).willReturn(cart);

        Executable executable = () -> service.addTraining(addTrainingTrainingToCartCommand(cartId, trainingId));

        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        assertThat(actual).hasMessage("Training: " + trainingId + " is already in the cart.");
    }

    private AddTrainingToCartCommand addTrainingTrainingToCartCommand(CartId cartId, TrainingId trainingId) {
        return new AddTrainingToCartCommand(cartId.value(), trainingId.value());
    }

    @Test
    void shouldRemoveTrainingFromCart() {
        CartId cartId = cartId();
        TrainingId trainingId = trainingId();
        Cart cart = new Cart(cartId);
        cart.add(trainingId);
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
        given(cartRepository.findBy(cartId)).willReturn(new Cart(cartId));

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
