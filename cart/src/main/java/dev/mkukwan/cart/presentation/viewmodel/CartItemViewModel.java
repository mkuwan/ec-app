package dev.mkukwan.cart.presentation.viewmodel;

import dev.mkukwan.cart.usecase.dto.CartItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.NumberFormat;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartItemViewModel {

    private String cartId;
    private String itemId;
    private String itemName;
    private long itemPrice;
    private String formattedItemPrice;
    private int itemAmount;
    private String itemAmountView;
    private int itemLimitedCount;
    private String limitedCountView;

    public CartItemDto toDto(){
        return new CartItemDto(this.cartId, this.itemId, this.itemName,
                this.itemPrice, this.itemAmount, this.itemLimitedCount);
    }

    public static CartItemViewModel fromDto(CartItemDto itemDto){
        var numberFormat = NumberFormat.getNumberInstance();
        return new CartItemViewModel(itemDto.getCartId(), itemDto.getItemId(), itemDto.getItemName(),
                itemDto.getItemPrice(),
                numberFormat.format(itemDto.getItemPrice()) + "円",
                itemDto.getItemAmount(),
                itemDto.getItemAmount() + "個",
                itemDto.getItemLimitedCount(),
                "購入できるのは" + itemDto.getItemLimitedCount() + "個までです");
    }
}
