package dev.mkuwan.adminstock.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "StockTable")
public class StockTable {

    @Id
    private String stockId;

    private String displayName;
    private long salesPrice;
    private int stockAmount;
    private int purchaseLimit;
    private float averageCostPrice;
    private String description;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    private Set<StockItemTable> stockItems;

    private String itemSerialNumber;
    private boolean canOrder;

    private LocalDateTime createdAt;

    @OneToOne
    private UserTable creator;

    private LocalDateTime updatedAt;

    @OneToOne
    private UserTable updater;
}
