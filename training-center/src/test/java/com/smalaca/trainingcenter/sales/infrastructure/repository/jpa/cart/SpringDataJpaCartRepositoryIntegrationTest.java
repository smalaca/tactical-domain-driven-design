package com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.cart;

import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.cart.CartRepository;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.opentrainingservice.OpenTrainingService;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.smalaca.trainingcenter.sales.domain.cart.CartAssertion.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DataJpaTest
@Import(SpringDataJpaCartRepository.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class SpringDataJpaCartRepositoryIntegrationTest {
    @Autowired private CartRepository cartRepository;
    @Autowired private JpaCartRepository jpaCartRepository;

    private final Clock clock = mock(Clock.class);
    private final OpenTrainingService openTrainingService = mock(OpenTrainingService.class);

    @AfterEach
    void tearDown() {
        jpaCartRepository.deleteAll();
    }

    @Test
    void shouldThrowExceptionWhenCartNotFound() {
        CartId cartId = new CartId(UUID.randomUUID());

        assertThatThrownBy(() -> cartRepository.findBy(cartId))
                .isInstanceOf(CartNotFoundException.class);
    }

    @Test
    void shouldFindExistingCart() {
        CartId cartId = new CartId(UUID.randomUUID());
        Cart cart = Cart.active(cartId);
        cartRepository.save(cart);

        Cart actual = cartRepository.findBy(cartId);

        assertThat(actual).hasId(cartId);
    }

    @Test
    void shouldFindSpecificCartWhenMultipleExist() {
        CartId cartId1 = new CartId(UUID.randomUUID());
        CartId cartId2 = new CartId(UUID.randomUUID());
        CartId cartId3 = new CartId(UUID.randomUUID());
        cartRepository.save(Cart.active(cartId1));
        cartRepository.save(Cart.active(cartId2));
        cartRepository.save(Cart.active(cartId3));

        Cart actual = cartRepository.findBy(cartId2);

        assertThat(actual).hasId(cartId2);
    }

    @Test
    void shouldSaveAndLoadCartWithItems() {
        LocalDateTime addedAtOne = LocalDateTime.now();
        LocalDateTime addedAtTwo = LocalDateTime.now();
        given(clock.now()).willReturn(addedAtOne, addedAtTwo);
        given(openTrainingService.hasAlreadyStarted(any())).willReturn(false);
        CartId cartId = new CartId(UUID.randomUUID());
        TrainingId trainingId1 = new TrainingId(UUID.randomUUID());
        TrainingId trainingId2 = new TrainingId(UUID.randomUUID());
        Cart cart = Cart.active(cartId);
        cart.add(trainingId1, clock, openTrainingService);
        cart.add(trainingId2, clock, openTrainingService);

        cartRepository.save(cart);

        Cart actual = cartRepository.findBy(cartId);
        assertThat(actual)
                .hasId(cartId)
                .hasTrainings(2)
                .hasTraining(trainingId1, addedAtOne)
                .hasTraining(trainingId2, addedAtTwo);
    }
}
