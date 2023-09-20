package dev.mkuwan.adminstock.usecase.service;


import dev.mkuwan.adminstock.domain.model.StockModel;
import dev.mkuwan.adminstock.domain.valueobject.StockItem;
import dev.mkuwan.adminstock.usecase.dto.StockDto;
import dev.mkuwan.adminstock.usecase.dto.StockItemDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IStockUsecaseService {

    List<StockDto> getStocks();

    StockDto getStock(String stockId);

    StockDto determineSalesPrice(String stockId, long rePrice);

    StockDto fixLimitedAmount(StockDto stock);

    StockDto specifyDisplayName(StockDto stock);

    void addStockItem(String stockId, StockItemDto item);

    void deleteStockItem(String stockId, StockItemDto item);

    void modifyStockItem(String stockId, StockItemDto item);
}
