package dev.mkuwan.adminstock.domain.valueobject;

import java.time.LocalDate;

/**
 *
 * @param stockItemId 在庫商品ID
 * @param itemName 商品名
 * @param amount 商品数
 * @param costPrice 仕入れ価格
 * @param localDate 仕入れ日
 * @param reason 変更事由
 * @param description 備考
 * @param wareHouse 倉庫
 */
public record StockItem(String stockItemId,
                        String itemName,
                        int amount,
                        long costPrice,
                        LocalDate localDate,
                        String reason,
                        String description,
                        WareHouse wareHouse) {

    public StockItem {
        if (amount < 0) {
            throw new IllegalArgumentException("商品数を0より小さくすることはできません");
        }

        if (costPrice < 0) {
            throw new IllegalArgumentException("仕入れ価格を0円より小さくすることはできません");
        }

        if (itemName == null || itemName.length() == 0) {
            throw new IllegalArgumentException("商品名がありません");
        }

    }
}

