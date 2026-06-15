package com.smalaca.trainingcenter.sales.query;

import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.cart.CartRepository;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.cart.SpringDataJpaCartRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.smalaca.trainingcenter.sales.query.CartViewAssertion.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({CartQueryService.class, SpringDataJpaCartRepository.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class CartQueryServiceIntegrationTest {
    @Autowired private CartQueryService queryService;
    @Autowired private JpaCartViewRepository viewRepository;
    @Autowired private CartRepository cartRepository;

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
        givenCartWithTrainings(cartId, trainingId1, trainingId2);

        Optional<CartView> actual = queryService.findOneById(cartId);

        assertThat(actual).isPresent();
        assertThat(actual.get())
                .hasId(cartId)
                .hasTrainings(2)
                .hasTraining(trainingId1)
                .hasTraining(trainingId2);
    }

    @Test
    void shouldFindAllCarts() {
        UUID cartId1 = id();
        UUID trainingId1 = id();
        givenCartWithTrainings(cartId1, trainingId1);
        UUID cartId2 = id();
        UUID trainingId2 = id();
        UUID trainingId3 = id();
        givenCartWithTrainings(cartId2, trainingId2, trainingId3);

        List<CartView> actual = queryService.findAll();

        assertThat(actual).hasSize(2);
        assertThat(actual)
                .anySatisfy(view -> assertThat(view)
                        .hasId(cartId1)
                        .hasTrainings(1)
                        .hasTraining(trainingId1))
                .anySatisfy(view -> assertThat(view)
                        .hasId(cartId2)
                        .hasTrainings(2)
                        .hasTraining(trainingId2)
                        .hasTraining(trainingId3));
    }

    private UUID id() {
        return UUID.randomUUID();
    }

    private void givenCartWithTrainings(UUID cartId, UUID... trainingIds) {
        Cart cart = new Cart(new CartId(cartId));
        for (UUID trainingId : trainingIds) {
            cart.add(new TrainingId(trainingId));
        }

        cartRepository.save(cart);
    }
}
