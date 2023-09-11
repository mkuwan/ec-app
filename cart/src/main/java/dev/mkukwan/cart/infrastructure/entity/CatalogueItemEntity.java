package dev.mkukwan.cart.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter // 本来は必要ありませんが、テスト用につけています
@Entity(name = "CatalogTable")
public class CatalogueItemEntity {
    @Id
    private String catalogItemId;
    private String catalogItemName;
    private long salesPrice;
    private int stockAmount;
    private int purchaseLimit;
}
