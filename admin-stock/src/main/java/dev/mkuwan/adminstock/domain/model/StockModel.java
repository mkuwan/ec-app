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
    private float averageCostPrice;
    private final String description;
    private final List<StockItem> stockItems;
    private final String itemSerialNumber;
    private final boolean canOrder;

    private final static float minimumSalesPriceRate = 1.5f;
    private final static float maximumSalesPriceRate = 3f;

    public StockModel(String stockId, DisplayName displayName, long salesPrice, int stockAmount,
                      int purchaseLimit, String description,
                      List<StockItem> stockItems, String itemSerialNumber, boolean canOrder,
                      LocalDateTime createdAt, StockUser creator, LocalDateTime updatedAt, StockUser updater) {
        super(createdAt, creator, updatedAt, updater);
        this.stockId = stockId;
        this.displayName = displayName;
        this.stockAmount = stockAmount;

        this.description = description;
        this.stockItems = stockItems;
        this.itemSerialNumber = itemSerialNumber;
        this.canOrder = canOrder;
        this.averageCostPrice = AverageCostPrice();

        fixLimitedAmount(purchaseLimit);
        determineSalesPrice(salesPrice);
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

    /**
     * 平均仕入れ価格は在庫のある商品の仕入れ価格の平均を四捨五入したもの
     * @return float(実際は整数値 nn.0とします)
     */
    public float AverageCostPrice() {
        var average = stockItems.stream()
                .filter(x -> x.amount() > 0)
                .mapToLong(x -> x.costPrice())
                .average();
        if(average.isPresent()){
            averageCostPrice = Math.round(average.getAsDouble());
        } else {
            averageCostPrice = 0f;
        }
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
        var average = averageCostPrice;
        if(average * minimumSalesPriceRate > salesPrice){
            var message = String.format("販売価格は平均仕入れ額%s円の%s倍以上としてください", (long)average, minimumSalesPriceRate);
            throw new IllegalArgumentException(message);
        }

        if(average * maximumSalesPriceRate < salesPrice){
            var message = String.format("販売価格は平均仕入れ額%s円の%s倍以下としてください", (long)average, maximumSalesPriceRate);
            throw new IllegalArgumentException(message);
        }

        this.salesPrice = salesPrice;
    }

    /**
     * 購入限度数
     * @param limitedAmount
     */
    public void fixLimitedAmount(int limitedAmount){
        if(limitedAmount <= 0){
            throw new IllegalArgumentException("購入限度数は1以上としてください");
        }

        this.purchaseLimit = limitedAmount;
    }

    public void addStockItem(StockItem item){
        this.StockItems().add(item);
    }

    public void deleteStockItem(StockItem item){
        this.StockItems().remove(item);
    }

    public void modifyStockItem(StockItem item){
        var existItem = this.stockItems
                .stream()
                .filter(x -> x.stockItemId() == item.stockItemId())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("指定された商品が見つかりませんでした"));

        this.stockItems.remove(existItem);
        this.stockItems.add(item);
    }
}
