package dev.mkukwan.cart.presentation.viewmodel;


import dev.mkukwan.cart.infrastructure.entity.CatalogueItemEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CatalogueViewModel {
    private String catalogItemId;
    private String catalogItemName;
    private long salesPrice;
    private int stockAmount;
    private int purchaseLimit;

    public static CatalogueViewModel fromEntity(CatalogueItemEntity entity){
        return new CatalogueViewModel(entity.getCatalogItemId(),
                entity.getCatalogItemName(),
                entity.getSalesPrice(),
                entity.getStockAmount(),
                entity.getPurchaseLimit());
    }
}
