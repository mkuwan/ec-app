package dev.mkukwan.cart.usecase.service;


import dev.mkukwan.cart.usecase.dto.CartDto;
import dev.mkukwan.cart.usecase.dto.CartItemDto;

public interface ICartUseCaseService {

    CartDto getCartByCartId(String cartId);

    CartDto putItemIntoCart(String cartId, CartItemDto cartItemDto);

    CartDto modifyCartItem(String cartId, CartItemDto cartItemDto);

}
