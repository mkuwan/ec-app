package dev.mkuwan.adminstock.domain.model;

import dev.mkuwan.adminstock.domain.valueobject.DisplayName;
import dev.mkuwan.adminstock.domain.valueobject.StockItem;
import dev.mkuwan.adminstock.domain.valueobject.StockUser;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 在庫ドメインモデル
 */
public class StockModel extends DomainBase implements IAggregateRoot {
    private final String stockId;
    private final DisplayName displayName;
    private long salesPrice;
    private final int stockAmount;
    private int purchaseLimit;
    private final float averageCostPrice;
    private final String description;
    private final List<StockItem> stockItems;
    private final String itemSerialNumber;
    private final boolean canOrder;

    private final static float minimumSalesPriceRate = 1.5f;
    private final static float maximumSalesPriceRate = 3f;

    public StockModel(String stockId, DisplayName displayName, long salesPrice, int stockAmount,
                      int purchaseLimit, float averageCostPrice, String description,
                      List<StockItem> stockItems, String itemSerialNumber, boolean canOrder,
                      LocalDateTime createdAt, StockUser creator, StockUser updater) {
        super(createdAt, creator, updater);
        this.stockId = stockId;
        this.displayName = displayName;
        this.salesPrice = salesPrice;
        this.stockAmount = stockAmount;
        this.purchaseLimit = purchaseLimit;
        this.averageCostPrice = averageCostPrice;
        this.description = description;
        this.stockItems = stockItems;
        this.itemSerialNumber = itemSerialNumber;
        this.canOrder = canOrder;

    }

    public String StockId() {
        return stockId;
    }

    public DisplayName DisplayName() {
        return displayName;
    }

    public long SalesPrice() {
        return salesPrice;
    }

    public int StockAmount() {
        return stockAmount;
    }

    public int PurchaseLimit() {
        return purchaseLimit;
    }

    public float AverageCostPrice() {
        return averageCostPrice;
    }

    public String Description() {
        return description;
    }

    public List<StockItem> StockItems() {
        return stockItems;
    }

    public String ItemSerialNumber() {
        return itemSerialNumber;
    }

    public boolean CanOrder() {
        return canOrder;
    }

    /**
     * 販売価格は平均仕入れ価格の1.5〜3倍の範囲
     * 在庫のある商品の平均で算出する
     * @param salesPrice
     */
    public void determineSalesPrice(long salesPrice){
        var average = (float)stockItems.stream()
                .mapToLong(x -> x.costPrice())
                .sum() / (float) stockItems.stream().count();
        if(average * minimumSalesPriceRate < salesPrice){
            var message = String.format("販売価格は平均仕入れ額{0}の{1}倍以上としてください", average, minimumSalesPriceRate);
            throw new IllegalArgumentException(message);
        }

        if(average * maximumSalesPriceRate > salesPrice){
            var message = String.format("販売価格は平均仕入れ額{0}の{1}倍以下としてください", average, maximumSalesPriceRate);
            throw new IllegalArgumentException(message);
        }

        this.salesPrice = salesPrice;
    }


    public void fixLimitedAmount(int amount){
        if(amount <= 0){
            throw new IllegalArgumentException("購入限度数は1以上としてください");
        }

        this.purchaseLimit = amount;
    }
}
