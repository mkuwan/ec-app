package dev.mkuwan.adminstock.usecase.service;


import dev.mkuwan.adminstock.domain.model.StockModel;
import dev.mkuwan.adminstock.domain.repository.IStockRepository;
import dev.mkuwan.adminstock.domain.valueobject.StockItem;
import dev.mkuwan.adminstock.domain.valueobject.StockUser;
import dev.mkuwan.adminstock.domain.valueobject.WareHouse;
import dev.mkuwan.adminstock.usecase.dto.StockDto;
import dev.mkuwan.adminstock.usecase.dto.StockItemDto;
import dev.mkuwan.adminstock.usecase.event.StockEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockUsecaseService implements IStockUsecaseService{
    private final StockEventPublisher publisher;
    private final IStockRepository stockRepository;

    public StockUsecaseService(StockEventPublisher publisher, IStockRepository stockRepository) {
        this.publisher = publisher;
        this.stockRepository = stockRepository;
    }


    @Override
    public List<StockDto> getStocks() {
        var stocks = stockRepository.getStocks();
        List<StockDto> stockDtoList = new ArrayList<>();
        if(stocks != null){
            stocks.forEach(x -> {
                stockDtoList.add(StockDto.fromModel(x));
            });
        }
        return stockDtoList;
    }

    @Override
    public void determineSalesPrice(StockDto stock) {

    }

    @Override
    public void fixLimitedAmount(StockDto stock) {

    }

    @Override
    public void specifyDisplayName(StockDto stock) {

    }


    @Override
    public void addStockItem(String stockId, StockItemDto item) {

    }

    @Override
    public void deleteStockItem(String stockId, StockItemDto item) {

    }

    @Override
    public void modifyStockItem(String stockId, StockItemDto item) {
        // get Domain Model
        var model = stockRepository.getStock(stockId);
        StockItem modifiedItem = new StockItem(item.getItemId(), item.getItemName(), item.getAmount(),
                item.getCostPrice(), item.getStockingDate(), item.getReason(),
                item.getDescription(), new WareHouse(item.getWareHouseId(), item.getWareHouseName()),
                item.getCreatedAt(), new StockUser(item.getCreatorId(), item.getUpdaterName()),
                LocalDateTime.now(), new StockUser(item.getUpdaterId(), item.getUpdaterName()));
        // update Domain Model
        model.modifyStockItem(modifiedItem);

        // Repository Save

    }
}
