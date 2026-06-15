package com.smalaca.trainingcenter.sales.infrastructure.repository.jpa.cart;

import com.smalaca.trainingcenter.sales.domain.cart.Cart;
import com.smalaca.trainingcenter.sales.domain.cart.CartId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface JpaCartRepository extends CrudRepository<Cart, CartId> {
}
