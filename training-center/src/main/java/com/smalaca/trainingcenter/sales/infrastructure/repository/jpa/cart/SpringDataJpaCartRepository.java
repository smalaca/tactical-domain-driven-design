package com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.cart;

import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;
import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.cart.CartRepository;
import org.springframework.stereotype.Component;

@PortsAndAdaptersArchitecture.DrivenAdapter
@Component
public class SpringDataJpaCartRepository implements CartRepository {
    private final JpaCartRepository jpaCartRepository;

    public SpringDataJpaCartRepository(JpaCartRepository jpaCartRepository) {
        this.jpaCartRepository = jpaCartRepository;
    }

    @Override
    public Cart findBy(CartId cartId) {
        return jpaCartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));
    }

    @Override
    public void save(Cart cart) {
        jpaCartRepository.save(cart);
    }
}
