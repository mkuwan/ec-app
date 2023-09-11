package dev.mkukwan.cart.presentation.viewmodel;

import dev.mkukwan.cart.usecase.dto.CartDto;
import dev.mkukwan.cart.usecase.dto.CartItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class CartViewModel {
    private String cartId;
    private String buyerId;
    private List<CartItemViewModel> cartItemViewList;

    private String message;

    public CartDto toDto(){
        List<CartItemDto> itemDtoList = new ArrayList<>();
        this.cartItemViewList.forEach(x -> {
            itemDtoList.add(new CartItemDto(this.cartId, x.getItemId(), x.getItemName(),
                    x.getItemPrice(), x.getItemAmount(), x.getItemLimitedCount()));
        });
        return new CartDto(this.cartId, this.buyerId, itemDtoList);
    }

    public static CartViewModel fromDto(CartDto cartDto){
        List<CartItemViewModel> itemViewModelList = new ArrayList<>();
        cartDto.getCartItemDtoList().forEach(x -> {
            itemViewModelList.add(CartItemViewModel.fromDto(x));
        });

        return new CartViewModel(cartDto.getCartId(), cartDto.getBuyerId(),itemViewModelList, null);
    }
}
