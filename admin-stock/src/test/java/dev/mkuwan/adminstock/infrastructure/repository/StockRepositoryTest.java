package dev.mkuwan.adminstock.infrastructure.repository;

import dev.mkuwan.adminstock.infrastructure.entity.StockItemTable;
import dev.mkuwan.adminstock.infrastructure.entity.WareHouseTable;
import dev.mkuwan.adminstock.infrastructure.jpa.IStockItemTableJpaRepository;
import dev.mkuwan.adminstock.infrastructure.jpa.IWareHouseTableJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private IWareHouseTableJpaRepository wareHouseTableJpaRepository;

    @Autowired
    private IStockItemTableJpaRepository stockItemTableJpaRepository;

    @Test
    void getStocks() {
        // arrange
        var item01 = StockItemTable.builder()
                .itemId(UUID.randomUUID().toString())
                .itemName("テスト商品A")
                .amount(10)
                .costPrice(1000)
                .description("テスト用商品Aです")
                .build();
        stockItemTableJpaRepository.save(item01);

        var item02 = StockItemTable.builder()
                .itemId(UUID.randomUUID().toString())
                .itemName("テスト商品B")
                .amount(5)
                .costPrice(1200)
                .description("テスト用商品Bです")
                .build();
        stockItemTableJpaRepository.save(item02);

        var items = new HashSet<StockItemTable>();
        items.add(item01);
        items.add(item02);
        var wareHouse = WareHouseTable.builder()
                .wareHouseId(UUID.randomUUID().toString())
                .wareHouseName("テスト倉庫A")
                .StockItems(items)
                .build();
        wareHouseTableJpaRepository.save(wareHouse);

    }

    @Test
    void getStock() {
    }


}