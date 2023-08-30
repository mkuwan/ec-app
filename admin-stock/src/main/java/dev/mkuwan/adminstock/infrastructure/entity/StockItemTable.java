package dev.mkuwan.adminstock.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "StockItemTable")
public class StockItemTable {

    @Id
    private String itemId;

    private String itemName;
    private int amount;
    private long costPrice;
    private LocalDate stockingDate;
    private String reason;
    private String description;

    private String stockId;
    private String wareHouseId;

    @ManyToOne
    private WareHouseTable wareHouseTable;

    private LocalDateTime createdAt;

    @OneToOne
    private UserTable creator;

    private LocalDateTime updatedAt;

    @OneToOne
    private UserTable updater;

}
