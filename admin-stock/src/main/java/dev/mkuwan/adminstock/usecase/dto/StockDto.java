package dev.mkuwan.adminstock.usecase.dto;


import dev.mkuwan.adminstock.domain.model.StockModel;
import dev.mkuwan.adminstock.domain.valueobject.DisplayName;
import dev.mkuwan.adminstock.domain.valueobject.StockItem;
import dev.mkuwan.adminstock.domain.valueobject.StockUser;
import dev.mkuwan.adminstock.domain.valueobject.WareHouse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder
public class StockDto {

    private String stockId;
    private String displayName;
    private long salesPrice;
    private int stockAmount;
    private int purchaseLimit;
    private float averageCostPrice;
    private String description;
    private List<StockItemDto> stockItems;
    private String itemSerialNumber;
    private boolean canOrder;
    private String creatorId;
    private String creatorName;
    private String updaterId;
    private String updaterName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StockModel toModel(){
        List<StockItem> itemList = new ArrayList<>();
        if(stockItems != null){
            stockItems.forEach(x -> {
                itemList.add(new StockItem(
                        x.getItemId(), x.getItemName(), x.getAmount(),
                        x.getCostPrice(), x.getStockingDate(), x.getReason(),
                        x.getDescription(),
                        new WareHouse(x.getWareHouseId(), x.getWareHouseName()),
                        x.getCreatedAt(), new StockUser(x.getCreatorId(), x.getCreatorName()),
                        x.getUpdatedAt(), new StockUser(x.getUpdaterId(), x.getUpdaterName())
                ));
            });
        }

        var model = new StockModel(this.stockId, new DisplayName(this.displayName), this.salesPrice, this.stockAmount,
                this.purchaseLimit, this.description, itemList,
                this.itemSerialNumber, this.canOrder,
                createdAt, new StockUser(this.creatorId, this.creatorName),
                updatedAt, new StockUser(this.updaterId, this.updaterName));

        return model;
    }


    public static StockDto fromModel(StockModel model){
        List<StockItemDto> stockItemDtoList = new ArrayList<>();
        if(model.StockItems() != null){
            model.StockItems().forEach(x -> {
                stockItemDtoList.add(StockItemDto.fromModel(x));
            });
        }

        StockDto dto = StockDto.builder()
                .stockId(model.StockId())
                .displayName(model.DisplayName().name())
                .salesPrice(model.SalesPrice())
                .stockAmount(model.StockAmount())
                .purchaseLimit(model.PurchaseLimit())
                .averageCostPrice(model.AverageCostPrice())
                .description(model.Description())
                .stockItems(stockItemDtoList)
                .itemSerialNumber(model.ItemSerialNumber())
                .canOrder(model.CanOrder())
                .creatorId(UserIsNullThenReturnNull(model.Creator()).userId())
                .creatorName(UserIsNullThenReturnNull(model.Creator()).userName())
                .updaterId(UserIsNullThenReturnNull(model.Updater()).userId())
                .updaterName(UserIsNullThenReturnNull(model.Updater()).userName())
                .createdAt(model.CreatedAt())
                .updatedAt(model.UpdatedAt())
                .build();

        return dto;
    }

    private static StockUser UserIsNullThenReturnNull(StockUser user){
        if(user == null)
            return new StockUser(null, null);

        return new StockUser(user.userId(), user.userName());
    }
}
