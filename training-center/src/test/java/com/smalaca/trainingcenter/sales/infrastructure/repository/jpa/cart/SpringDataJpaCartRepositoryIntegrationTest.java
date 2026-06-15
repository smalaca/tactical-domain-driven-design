package com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.cart;

import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.cart.CartRepository;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.smalaca.trainingcenter.sales.domain.cart.CartAssertion.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(SpringDataJpaCartRepository.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class SpringDataJpaCartRepositoryIntegrationTest {
    @Autowired private CartRepository cartRepository;
    @Autowired private JpaCartRepository jpaCartRepository;

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
        Cart cart = new Cart(cartId);
        cartRepository.save(cart);

        Cart actual = cartRepository.findBy(cartId);

        assertThat(actual).hasId(cartId);
    }

    @Test
    void shouldFindSpecificCartWhenMultipleExist() {
        CartId cartId1 = new CartId(UUID.randomUUID());
        CartId cartId2 = new CartId(UUID.randomUUID());
        CartId cartId3 = new CartId(UUID.randomUUID());
        cartRepository.save(new Cart(cartId1));
        cartRepository.save(new Cart(cartId2));
        cartRepository.save(new Cart(cartId3));

        Cart actual = cartRepository.findBy(cartId2);

        assertThat(actual).hasId(cartId2);
    }

    @Test
    void shouldSaveAndLoadCartWithItems() {
        CartId cartId = new CartId(UUID.randomUUID());
        TrainingId trainingId1 = new TrainingId(UUID.randomUUID());
        TrainingId trainingId2 = new TrainingId(UUID.randomUUID());
        Cart cart = new Cart(cartId);
        cart.add(trainingId1);
        cart.add(trainingId2);

        cartRepository.save(cart);

        Cart actual = cartRepository.findBy(cartId);
        assertThat(actual)
                .hasId(cartId)
                .hasTrainings(2)
                .hasTraining(trainingId1)
                .hasTraining(trainingId2);
    }
}
