package dev.mkukwan.cart.domain.repository;


import dev.mkukwan.cart.domain.model.CartModel;
import dev.mkukwan.cart.domain.model.IAggregate;

public interface ICartRepository {
    void save(IAggregate cartModel);

    CartModel getCartByCartId(String cartId);

    CartModel getCartByBuyerId(String buyerId);
}
