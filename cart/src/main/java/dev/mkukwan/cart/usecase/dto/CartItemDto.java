package dev.mkukwan.cart.usecase.dto;

import dev.mkukwan.cart.domain.valueobject.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CartItemDto {
    private String cartId;
    private String itemId;
    private String itemName;
    private long itemPrice;
    private int itemAmount;
    private int itemLimitedCount;

    public static CartItemDto fromItemModel(CartItem itemModel){
        return CartItemDto.builder()
                .cartId(itemModel.cartId())
                .itemId(itemModel.itemId())
                .itemName(itemModel.itemName())
                .itemPrice(itemModel.expectedPrice())
                .itemAmount(itemModel.expectedAmount())
                .itemLimitedCount(itemModel.itemLimitedCount())
                .build();
    }
}
