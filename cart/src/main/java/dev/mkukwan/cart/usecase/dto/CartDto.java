package dev.mkukwan.cart.usecase.dto;

import dev.mkukwan.cart.domain.model.CartModel;
import dev.mkukwan.cart.domain.valueobject.Buyer;
import dev.mkukwan.cart.domain.valueobject.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class CartDto {
    private String cartId;
    private String buyerId;
    private List<CartItemDto> cartItemDtoList;


    public static CartDto fromCartModel(CartModel model){
        return CartDto.builder()
                .cartId(model.CartId())
                .buyerId(model.Buyer().buyerId())
                .cartItemDtoList(fromCartItem(model.CartItems()))
                .build();
    }

    private static List<CartItemDto> fromCartItem(List<CartItem> items){
        List<CartItemDto> resultItems = new ArrayList<>();

        items.forEach(x -> {
            CartItemDto itemDto = new CartItemDto(x.cartId(), x.itemId(), x.itemName(),
                    x.expectedPrice(), x.expectedAmount(), x.itemLimitedCount());
            resultItems.add(itemDto);
        });

        return resultItems;
    }

    public CartModel toCartModel(){
        return new CartModel(this.cartId, new Buyer(this.buyerId), fromCartItemDto());
    }

    private List<CartItem> fromCartItemDto(){
        List<CartItem> resultItems = new ArrayList<>();

        this.cartItemDtoList.forEach(x -> {
            resultItems.add(new CartItem(x.getCartId(), x.getItemId(), x.getItemName(),
                    x.getItemPrice(), x.getItemAmount(), x.getItemLimitedCount()));
        });

        return resultItems;
    }
}
