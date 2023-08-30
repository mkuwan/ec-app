package dev.mkuwan.adminstock.infrastructure.repository;

import dev.mkuwan.adminstock.domain.model.StockModel;
import dev.mkuwan.adminstock.domain.repository.IStockRepository;
import dev.mkuwan.adminstock.domain.valueobject.DisplayName;
import dev.mkuwan.adminstock.domain.valueobject.StockItem;
import dev.mkuwan.adminstock.domain.valueobject.StockUser;
import dev.mkuwan.adminstock.domain.valueobject.WareHouse;
import dev.mkuwan.adminstock.infrastructure.entity.StockItemTable;
import dev.mkuwan.adminstock.infrastructure.entity.StockTable;
import dev.mkuwan.adminstock.infrastructure.entity.UserTable;
import dev.mkuwan.adminstock.infrastructure.entity.WareHouseTable;
import dev.mkuwan.adminstock.infrastructure.jpa.IStockTableJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class StockRepository implements IStockRepository {

    private final IStockTableJpaRepository stockTableJpaRepository;

    public StockRepository(IStockTableJpaRepository stockTableJpaRepository) {
        this.stockTableJpaRepository = stockTableJpaRepository;
    }

    @Override
    public List<StockModel> getStocks() {
        var stocks = stockTableJpaRepository.findAll();

        List<StockModel> stockModels = new ArrayList<>();
        stocks.forEach(x -> {
            stockModels.add(fromTable(x));
        });

        return stockModels;
    }

    @Override
    public StockModel getStock(String stockId) {
        var stock = stockTableJpaRepository.findById(stockId);
        if(!stock.isPresent())
            return null;

        var stockEntity = stock.get();
        return fromTable(stockEntity);
    }

    private StockModel fromTable(StockTable stockTable){
        if(stockTable == null)
            throw new IllegalStateException("テーブルデータが初期化されていません");

        List<StockItem> stockItems = new ArrayList<>();
        if(stockTable.getStockItems() != null){
            stockTable.getStockItems().forEach(x -> {
                stockItems.add(new StockItem(x.getItemId(), x.getItemName(), x.getAmount(),
                        x.getCostPrice(), x.getStockingDate(), x.getReason(),
                        x.getDescription(), new WareHouse(x.getWareHouseId(), x.getWareHouseId()),
                        x.getCreatedAt(), new StockUser(x.getCreator().getUserId(), x.getCreator().getUserName()),
                        x.getUpdatedAt(), new StockUser(x.getUpdater().getUserId(), x.getUpdater().getUserName())));
            });
        }

        return new StockModel(stockTable.getStockId(), new DisplayName(stockTable.getDisplayName()),
                stockTable.getSalesPrice(), stockTable.getStockAmount(), stockTable.getPurchaseLimit(),
                stockTable.getDescription(), stockItems, stockTable.getItemSerialNumber(),
                stockTable.isCanOrder(),
                stockTable.getCreatedAt(), new StockUser(stockTable.getCreator().getUserId(), stockTable.getCreator().getUserName()),
                stockTable.getUpdatedAt(), new StockUser(stockTable.getUpdater().getUserId(), stockTable.getUpdater().getUserName()));
    }

    private StockTable toTable(StockModel model){
        Set<StockItemTable> items = new HashSet<>();
        model.StockItems().forEach(x -> {
            items.add(StockItemTable.builder()
                    .itemId(x.stockItemId())
                    .itemName(x.itemName())
                    .amount(x.amount())
                    .costPrice(x.costPrice())
                    .stockingDate(x.stockingDate())
                    .reason(x.reason())
                    .description(x.description())
                    .wareHouseTable(WareHouseTable.builder()
                            .wareHouseId(x.wareHouse().wareHouseId())
                            .wareHouseName(x.wareHouse().wareHouseName())
                            .build())
                    .createdAt(x.createdAt())
                    .creator(UserTable.builder()
                            .userId(x.creator().userId())
                            .userName(x.creator().userName())
                            .build())
                    .updatedAt(x.updatedAt())
                    .updater(UserTable.builder()
                            .userId(x.updater().userId())
                            .userName(x.updater().userName())
                            .build())
                    .build()
            );
        });

        StockTable entity = StockTable.builder()
                .stockId(model.StockId())
                .displayName(model.DisplayName().name())
                .salesPrice(model.SalesPrice())
                .stockAmount(model.StockAmount())
                .purchaseLimit(model.PurchaseLimit())
                .description(model.Description())
                .stockItems(items)
                .itemSerialNumber(model.ItemSerialNumber())
                .canOrder(model.CanOrder())
                .createdAt(model.CreatedAt())
                .creator(UserTable.builder()
                        .userId(model.Creator().userId())
                        .userName(model.Creator().userName())
                        .build())
                .updatedAt(model.UpdatedAt())
                .updater(UserTable.builder()
                        .userId(model.Updater().userId())
                        .userName(model.Updater().userName())
                        .build())
                .build();

        return entity;
    }


    @Override
    public void save(StockModel stockModel) {
        var entity = toTable(stockModel);
        stockTableJpaRepository.save(entity);
    }
}
