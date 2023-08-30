package dev.mkuwan.adminstock.domain.repository;

import dev.mkuwan.adminstock.domain.model.StockModel;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface IStockRepository {
    List<StockModel> getStocks();

    StockModel getStock(String stockId);

    void save(StockModel stockModel);
}
