package com.smalaca.trainingcenter.sales.infrastructure.persistence.jpa;

import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartAssertion;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.cart.CartRepository;
import com.smalaca.trainingcenter.sales.domain.training.TrainingId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

@DataJpaTest
@Import(SpringDataJpaCartRepository.class)
class SpringDataJpaCartRepositoryIntegrationTest {
    @Autowired private CartRepository cartRepository;

    @Test
    void shouldSaveAndLoadCart() {
        CartId cartId = new CartId(UUID.randomUUID());
        TrainingId trainingId1 = new TrainingId(UUID.randomUUID());
        TrainingId trainingId2 = new TrainingId(UUID.randomUUID());
        Cart cart = new Cart(cartId);
        cart.add(trainingId1);
        cart.add(trainingId2);

        cartRepository.save(cart);

        Cart actual = cartRepository.findBy(cartId);
        CartAssertion.assertThat(actual)
                .hasId(cartId)
                .hasTrainings(2)
                .hasTraining(trainingId1)
                .hasTraining(trainingId2);
    }
}
