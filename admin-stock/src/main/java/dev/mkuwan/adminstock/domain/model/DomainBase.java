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

    public DomainBase(LocalDateTime createdAt, StockUser creator, StockUser updater) {
        this.createdAt = createdAt;
        this.creator = creator;

        /**
         * 更新者がある場合は更新日時・更新者をセットします
         */
        if(updater != null){
            this.updatedAt = LocalDateTime.now();
            this.updater = updater;
        } else {
            this.updatedAt = null;
            this.updater = null;
        }
    }


}
