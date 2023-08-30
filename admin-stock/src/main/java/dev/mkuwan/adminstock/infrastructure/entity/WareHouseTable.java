package dev.mkuwan.adminstock.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "WareHouseTable")
public class WareHouseTable {

    @Id
    private String wareHouseId;
    private String wareHouseName;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    private Set<StockItemTable> StockItems;
}
