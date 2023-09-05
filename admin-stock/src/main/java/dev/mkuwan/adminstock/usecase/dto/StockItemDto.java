package dev.mkuwan.adminstock.usecase.dto;

import dev.mkuwan.adminstock.domain.valueobject.StockItem;
import dev.mkuwan.adminstock.domain.valueobject.StockUser;
import dev.mkuwan.adminstock.domain.valueobject.WareHouse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class StockItemDto {
    private String itemId;

    private String itemName;
    private int amount;
    private long costPrice;
    private LocalDate stockingDate;
    private String reason;
    private String description;

    private String wareHouseId;
    private String wareHouseName;

    private String creatorId;
    private String creatorName;
    private String updaterId;
    private String updaterName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StockItem toModel(){
        return new StockItem(this.itemId, this.itemName, this.amount,
                this.costPrice, this.stockingDate, this.reason, this.description,
                new WareHouse(this.wareHouseId, this.wareHouseName),
                this.createdAt, new StockUser(this.creatorId, this.creatorName),
                this.updatedAt, new StockUser(this.updaterId, this.updaterName));
    }

    public static StockItemDto fromModel(StockItem item){
        return StockItemDto.builder()
                .itemId(item.stockItemId())
                .itemName(item.itemName())
                .amount(item.amount())
                .costPrice(item.costPrice())
                .stockingDate(item.stockingDate())
                .reason(item.reason())
                .description(item.description())
                .wareHouseId(item.wareHouse().wareHouseId())
                .wareHouseName(item.wareHouse().wareHouseName())
                .createdAt(item.createdAt())
                .creatorId(UserIsNullThenReturnNull(item.creator()).userId())
                .creatorName(UserIsNullThenReturnNull(item.creator()).userName())
                .updatedAt(item.updatedAt())
                .updaterId(UserIsNullThenReturnNull(item.updater()).userId())
                .updaterName(UserIsNullThenReturnNull(item.updater()).userName())
                .build();
    }

    private static StockUser UserIsNullThenReturnNull(StockUser user){
        if(user == null)
            return new StockUser(null, null);

        return new StockUser(user.userId(), user.userName());
    }
}
