package com.smalaca.trainingcenter.sales.domain.cart;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class CartItemTest {
    @Test
    void shouldVerifyEqualsAndHashCode() {
        EqualsVerifier.forClass(CartItem.class)
                .suppress(nl.jqno.equalsverifier.Warning.STRICT_INHERITANCE)
                .verify();
    }
}