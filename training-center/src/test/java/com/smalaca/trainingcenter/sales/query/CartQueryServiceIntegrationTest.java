package com.smalaca.trainingcenter.sales.query;

import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.cart.CartRepository;
import com.smalaca.trainingcenter.sales.domain.clock.Clock;
import com.smalaca.trainingcenter.sales.domain.opentrainingservice.OpenTrainingService;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.cart.SpringDataJpaCartRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.smalaca.trainingcenter.sales.query.CartViewAssertion.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DataJpaTest
@Import({CartQueryService.class, SpringDataJpaCartRepository.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class CartQueryServiceIntegrationTest {
    @Autowired private CartQueryService queryService;
    @Autowired private JpaCartViewRepository viewRepository;
    @Autowired private CartRepository cartRepository;

    private final Clock clock = mock(Clock.class);
    private final OpenTrainingService openTrainingService = mock(OpenTrainingService.class);

    @AfterEach
    void tearDown() {
        viewRepository.deleteAll();
    }

    @Test
    void shouldFindNothingWhenNoCarts() {
        UUID cartId = id();

        Optional<CartView> actual = queryService.findOneById(cartId);

        assertThat(actual).isEmpty();
    }

    @Test
    void shouldFindCartById() {
        UUID cartId = id();
        UUID trainingId1 = id();
        UUID trainingId2 = id();
        LocalDateTime addedAt1 = LocalDateTime.now().minusMinutes(10);
        LocalDateTime addedAt2 = LocalDateTime.now().minusMinutes(5);
        givenCartWithTrainings(cartId, trainingId1, addedAt1, trainingId2, addedAt2);

        Optional<CartView> actual = queryService.findOneById(cartId);

        assertThat(actual).isPresent();
        assertThat(actual.get())
                .hasId(cartId)
                .hasTrainings(2)
                .hasTraining(trainingId1, addedAt1)
                .hasTraining(trainingId2, addedAt2);
    }

    @Test
    void shouldFindAllCarts() {
        UUID cartId1 = id();
        UUID trainingId1 = id();
        LocalDateTime addedAt1 = LocalDateTime.now().minusMinutes(10);
        givenCartWithTrainings(cartId1, trainingId1, addedAt1);
        UUID cartId2 = id();
        UUID trainingId2 = id();
        UUID trainingId3 = id();
        LocalDateTime addedAt2 = LocalDateTime.now().minusMinutes(5);
        LocalDateTime addedAt3 = LocalDateTime.now().minusMinutes(1);
        givenCartWithTrainings(cartId2, trainingId2, addedAt2, trainingId3, addedAt3);

        List<CartView> actual = queryService.findAll();

        assertThat(actual).hasSize(2);
        assertThat(actual)
                .anySatisfy(view -> assertThat(view)
                        .hasId(cartId1)
                        .hasTrainings(1)
                        .hasTraining(trainingId1, addedAt1))
                .anySatisfy(view -> assertThat(view)
                        .hasId(cartId2)
                        .hasTrainings(2)
                        .hasTraining(trainingId2, addedAt2)
                        .hasTraining(trainingId3, addedAt3));
    }

    private UUID id() {
        return UUID.randomUUID();
    }

    private void givenCartWithTrainings(UUID cartId, Object... trainingIdsAndAddedAt) {
        given(openTrainingService.hasAlreadyStarted(any())).willReturn(false);

        Cart cart = Cart.active(new CartId(cartId));
        for (int i = 0; i < trainingIdsAndAddedAt.length; i += 2) {
            UUID trainingId = (UUID) trainingIdsAndAddedAt[i];
            LocalDateTime addedAt = (LocalDateTime) trainingIdsAndAddedAt[i + 1];
            given(clock.now()).willReturn(addedAt);

            cart.add(new TrainingId(trainingId), clock, openTrainingService);
        }

        cartRepository.save(cart);
    }
}
