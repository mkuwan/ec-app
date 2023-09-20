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

    /**
     * 在庫商品リストを取得
     * @return
     */
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

    /**
     * 在庫商品を1つ取得
     * @param stockId
     * @return
     */
    @Override
    public StockDto getStock(String stockId) {
        var stock = stockRepository.getStock(stockId);

        if(stock == null)
            return null;

        return StockDto.fromModel(stock);
    }

    @Override
    public StockDto determineSalesPrice(String stockId, long rePrice) {
        var stockModel = stockRepository.getStock(stockId);
        stockModel.determineSalesPrice(rePrice);
        return StockDto.fromModel(stockModel);
    }

    @Override
    public StockDto fixLimitedAmount(StockDto stock) {
        return null;
    }

    @Override
    public StockDto specifyDisplayName(StockDto stock) {
        return null;
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
        stockRepository.save(model);
    }
}
