package dev.mkuwan.adminstock.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
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

    @OneToMany //(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    private Set<StockItemTable> stockItems;

    private String itemSerialNumber;
    private boolean canOrder;

    private LocalDateTime createdAt;

    @ManyToOne
    private UserTable creator;


    private LocalDateTime updatedAt;

    @ManyToOne
    private UserTable updater;
}
