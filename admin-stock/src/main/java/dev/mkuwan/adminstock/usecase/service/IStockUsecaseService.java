package dev.mkuwan.adminstock.usecase.service;


import dev.mkuwan.adminstock.domain.model.StockModel;
import dev.mkuwan.adminstock.domain.valueobject.StockItem;
import dev.mkuwan.adminstock.usecase.dto.StockDto;
import dev.mkuwan.adminstock.usecase.dto.StockItemDto;

import java.util.List;

public interface IStockUsecaseService {

    List<StockDto> getStocks();

    void determineSalesPrice(StockDto stock);

    void fixLimitedAmount(StockDto stock);

    void specifyDisplayName(StockDto stock);

    void addStockItem(String stockId, StockItemDto item);

    void deleteStockItem(String stockId, StockItemDto item);

    void modifyStockItem(String stockId, StockItemDto item);
}
