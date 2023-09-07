package dev.mkukwan.cart.domain.valueobject;

/**
 * カートの中身たち
 * @param cartId カートキー
 * @param itemId 商品ID
 * @param itemName 商品名
 * @param expectedPrice 購入予定価格
 * @param expectedAmount 購入予定数
 * @param itemLimitedCount 商品の購入限度数
 */
public record CartItem(String cartId,
                       String itemId,
                       String itemName,
                       long expectedPrice,
                       int expectedAmount,
                       int itemLimitedCount) {
}
