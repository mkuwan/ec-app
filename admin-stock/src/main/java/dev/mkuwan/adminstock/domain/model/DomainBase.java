package dev.mkuwan.adminstock.domain.model;

import dev.mkuwan.adminstock.domain.valueobject.StockUser;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * ドメインモデルの基底クラス
 */
public abstract class DomainBase {
    private final LocalDateTime createdAt;
    private final StockUser creator;
    private final LocalDateTime updatedAt;
    private final StockUser updater;

    public DomainBase(LocalDateTime createdAt, StockUser creator, LocalDateTime updatedAt, StockUser updater) {
        this.createdAt = createdAt;
        this.creator = creator;
        this.updatedAt = updatedAt;
        this.updater = updater;

    }

    public LocalDateTime CreatedAt(){
        return createdAt;
    }

    public StockUser Creator(){
        return creator;
    }

    public LocalDateTime UpdatedAt(){
        return updatedAt;
    }

    public StockUser Updater(){
        return updater;
    }
}
