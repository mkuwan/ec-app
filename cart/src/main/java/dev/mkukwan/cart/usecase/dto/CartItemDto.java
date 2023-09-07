package dev.mkukwan.cart.usecase.dto;

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
}
