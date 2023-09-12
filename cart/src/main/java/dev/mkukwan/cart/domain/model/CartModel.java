package dev.mkukwan.cart.domain.model;


import dev.mkukwan.cart.domain.valueobject.Buyer;
import dev.mkukwan.cart.domain.valueobject.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartModel implements IAggregate{

    private final String cartId;
    private final Buyer buyer;
    private final List<CartItem> cartItems;

    public CartModel(String cartId, Buyer buyer, List<CartItem> cartItems) {
        // カートを取得した際にIDがないものは新規として扱う
        if(cartId == null || cartId.equals(""))
            this.cartId = UUID.randomUUID().toString();
        else
            this.cartId = cartId;

        // 購入者のIDがない場合IDの割り振る
        if(buyer == null || buyer.buyerId() == null || buyer.buyerId().equals(""))
            this.buyer = new Buyer(UUID.randomUUID().toString());
        else
            this.buyer = buyer;

        if(cartItems == null)
            this.cartItems = new ArrayList<>();
        else{
            // 既存と入れ替えしてますが不要かも
//            List<CartItem> items = new ArrayList<>();
//            cartItems.forEach(x -> {
//                items.add(new CartItem(this.cartId, x.itemId(), x.itemName(),
//                        x.expectedPrice(), x.expectedAmount(), x.itemLimitedCount()));
//            });
//            this.cartItems = items;
            this.cartItems = cartItems;
        }

    }

    public String CartId() {
        return cartId;
    }

    public Buyer Buyer() {
        return buyer;
    }

    public List<CartItem> CartItems() {
        return cartItems;
    }

    public void putItemIntoCart(CartItem item){
        // カートに同じ商品があるか確認する
        var existedSameItem = cartItems
                .stream()
                .filter(x -> x.itemId().equals(item.itemId()))
                .findFirst().orElse(null);

        // カートに同じ商品がある場合は既存のリストデータを削除して新しいもので追加して更新します
        // こうすることで、もし新しい価格で更新されたりした場合に対応できます
        if(existedSameItem != null){
            var amount = existedSameItem.expectedAmount();
            var resultAmount = amount + item.expectedAmount();

            // 購入可能限度数を超えてカートに入れることはできない
            if(resultAmount > item.itemLimitedCount()){
                var message = String.format("購入限度数%sを超えてカートに入れることはできません", item.itemLimitedCount());
                throw new IllegalArgumentException(message);
            }

            cartItems.remove(existedSameItem);
            cartItems.add(new CartItem(
                    item.cartId(),
                    item.itemId(),
                    item.itemName(),
                    item.expectedPrice(),
                    resultAmount,
                    item.itemLimitedCount()));
        } else {
            // カートに同じ商品がない場合
            // カートの中に10商品ある場合はそれ以上は追加できない
            if(cartItems.size() == 10){
                throw new IllegalArgumentException("カートには10商品を超えて入れることはできません");
            }

            // 購入可能限度数を超えてカートに入れることはできない
            if(item.expectedAmount() > item.itemLimitedCount()){
                var message = String.format("購入限度数%sを超えてカートに入れることはできません", item.itemLimitedCount());
                throw new IllegalArgumentException(message);
            }
            cartItems.add(item);
        }

    }

    /**
     * カート画面でカートの中の商品の希望購入数の変更をした場合
     * 既存と入れ替えをします
     * @param item
     */
    public void modifyItemInCart(CartItem item){
        // 購入限度数の確認
        if(item.expectedAmount() > item.itemLimitedCount()){
            var message = String.format("購入限度数%sを超えてカートに入れることはできません", item.itemLimitedCount());
            throw new IllegalArgumentException(message);
        }

        // 既存と入れ替えをします
        var existedItem = cartItems
                .stream()
                .filter(x -> x.itemId().equals(item.itemId()))
                .findFirst()
                .get();

        cartItems.remove(existedItem);

        // 0個の場合は削除のみ
        if(item.expectedAmount() != 0)
            cartItems.add(item);
        else
            System.out.println("商品数が0のためカートから削除します");
    }

    public void clearCart(){
        cartItems.clear();
    }
}
