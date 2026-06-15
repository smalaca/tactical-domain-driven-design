package com.smalaca.trainingcenter.sales.infrastructure.persistence.jpa;

import com.smalaca.annotations.architecture.PortsAndAdaptersArchitecture;
import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import com.smalaca.trainingcenter.sales.domain.cart.CartRepository;
import org.springframework.stereotype.Component;

@PortsAndAdaptersArchitecture.DrivenAdapter
@Component
class SpringDataJpaCartRepository implements CartRepository {
    private final JpaCartRepository jpaCartRepository;

    SpringDataJpaCartRepository(JpaCartRepository jpaCartRepository) {
        this.jpaCartRepository = jpaCartRepository;
    }

    @Override
    public Cart findBy(CartId cartId) {
        return jpaCartRepository.findById(cartId).orElseThrow();
    }

    @Override
    public void save(Cart cart) {
        jpaCartRepository.save(cart);
    }
}
