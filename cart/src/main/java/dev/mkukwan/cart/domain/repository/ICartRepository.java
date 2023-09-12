package dev.mkukwan.cart.domain.repository;


import dev.mkukwan.cart.domain.model.CartModel;
import dev.mkukwan.cart.domain.model.IAggregate;
import dev.mkukwan.cart.domain.valueobject.CartItem;

public interface ICartRepository {
    void save(IAggregate cartModel);

    void clearCartAndSave(IAggregate cartModel);

    CartModel getCartByCartId(String cartId);

    CartModel getCartByBuyerId(String buyerId);

    CartItem getItemByItemIdFromCatalog(String itemId);
}
