package dev.mkuwan.adminstock.infrastructure.repository;

import dev.mkuwan.adminstock.infrastructure.entity.StockItemTable;
import dev.mkuwan.adminstock.infrastructure.entity.StockTable;
import dev.mkuwan.adminstock.infrastructure.entity.UserTable;
import dev.mkuwan.adminstock.infrastructure.entity.WareHouseTable;
import dev.mkuwan.adminstock.infrastructure.jpa.IStockItemTableJpaRepository;
import dev.mkuwan.adminstock.infrastructure.jpa.IStockTableJpaRepository;
import dev.mkuwan.adminstock.infrastructure.jpa.IUserTableJpaRepository;
import dev.mkuwan.adminstock.infrastructure.jpa.IWareHouseTableJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private IUserTableJpaRepository userTableJpaRepository;

    @Autowired
    private IStockTableJpaRepository stockTableJpaRepository;

    @Autowired
    private IWareHouseTableJpaRepository wareHouseTableJpaRepository;

    @Autowired
    private IStockItemTableJpaRepository stockItemTableJpaRepository;

    String user01Id = UUID.randomUUID().toString();
    String user01Name = "ユーザー1";
    String user02Id = UUID.randomUUID().toString();
    String user02Name = "ユーザー2";
    String itemA01Id = UUID.randomUUID().toString();
    String itemA01Name = "テスト商品A";
    String itemA02Id = UUID.randomUUID().toString();
    String itemA02Name = "テスト商品A2";
    String itemB01Id = UUID.randomUUID().toString();
    String itemB01Name = "テスト商品B1";
    String wareHouseId = UUID.randomUUID().toString();
    String wareHouseName = "第一倉庫";
    String StockAId = UUID.randomUUID().toString();
    String StockADisplayName = "商品A";
    String StockASerialNumber = "TEST-A-00000";

    @BeforeEach
    void setup(){

        var user01 = UserTable.builder()
                .userId(user01Id)
                .userName(user01Name)
                .build();
        userTableJpaRepository.save(user01);

        var user02 = UserTable.builder()
                .userId(user02Id)
                .userName(user02Name)
                .build();
        userTableJpaRepository.save(user02);

        var itemA01 = StockItemTable.builder()
                .itemId(itemA01Id)
                .itemName(itemA01Name)
                .amount(10)
                .costPrice(1000)
                .description("テスト用商品Aです")
                .creator(user01)
                .createdAt(LocalDateTime.now())
                .updater(user02)
                .updatedAt(LocalDateTime.now())
                .build();
        stockItemTableJpaRepository.save(itemA01);

        var itemA02 = StockItemTable.builder()
                .itemId(itemA02Id)
                .itemName(itemA02Name)
                .amount(20)
                .costPrice(800)
                .description("テスト用商品Aです")
                .build();
        stockItemTableJpaRepository.save(itemA02);

        var itemB = StockItemTable.builder()
                .itemId(itemB01Id)
                .itemName(itemB01Name)
                .amount(5)
                .costPrice(1200)
                .description("テスト用商品Bです")
                .build();
        stockItemTableJpaRepository.save(itemB);

        var itemsAtWareHouse = new HashSet<StockItemTable>();
        itemsAtWareHouse.add(itemA01);
        itemsAtWareHouse.add(itemA02);
        itemsAtWareHouse.add(itemB);
        var wareHouse = WareHouseTable.builder()
                .wareHouseId(wareHouseId)
                .wareHouseName(wareHouseName)
                .StockItems(itemsAtWareHouse)
                .build();
        wareHouseTableJpaRepository.save(wareHouse);
        itemA01.setWareHouseTable(wareHouse);
        stockItemTableJpaRepository.save(itemA01);
        itemA02.setWareHouseTable(wareHouse);
        stockItemTableJpaRepository.save(itemA02);
        itemB.setWareHouseTable(wareHouse);
        stockItemTableJpaRepository.save(itemB);

        var items = new HashSet<StockItemTable>();
        items.add(itemA01);
        items.add(itemA02);
        var stockA = StockTable.builder()
                .stockId(StockAId)
                .displayName(StockADisplayName)
                .stockAmount(2)
                .stockItems(items)
                .averageCostPrice(867)
                .purchaseLimit(3)
                .salesPrice(1500)
                .description("テスト用の説明ですよ")
                .itemSerialNumber(StockASerialNumber)
                .canOrder(true)
                .createdAt(LocalDateTime.now())
                .creator(user01)
                .updatedAt(LocalDateTime.now())
                .updater(user02)
                .build();
        stockTableJpaRepository.save(stockA);
    }

    @AfterEach
    void tearDown(){
        stockTableJpaRepository.deleteAll();
        stockItemTableJpaRepository.deleteAll();
        wareHouseTableJpaRepository.deleteAll();
        userTableJpaRepository.deleteAll();
    }

    @Test
    void getStocks_AddOtherItem_Success() {
        // arrange
        var user01 = userTableJpaRepository.findById(user01Id).get();
        var user02 = userTableJpaRepository.findById(user02Id).get();
        var itemOther = StockItemTable.builder()
                .itemId(UUID.randomUUID().toString())
                .itemName("別商品")
                .amount(10)
                .costPrice(1000)
                .description("テスト用商品Addです")
                .creator(user01)
                .createdAt(LocalDateTime.now())
                .updater(user02)
                .updatedAt(LocalDateTime.now())
                .build();
        stockItemTableJpaRepository.save(itemOther);

        var wareHouse = wareHouseTableJpaRepository.findById(wareHouseId).get();
        wareHouse.getStockItems().add(itemOther);
        wareHouseTableJpaRepository.save(wareHouse);

        itemOther.setWareHouseTable(wareHouse);
        stockItemTableJpaRepository.save(itemOther);

        var stockAddId = UUID.randomUUID().toString();
        var items = new HashSet<StockItemTable>();
        items.add(itemOther);
        var stockOther = StockTable.builder()
                .stockId(stockAddId)
                .displayName("別の商品")
                .stockAmount(10)
                .stockItems(items)
                .averageCostPrice(1000)
                .purchaseLimit(3)
                .salesPrice(2000)
                .description("テスト用の説明ですよ")
                .itemSerialNumber("ADD-010101")
                .canOrder(true)
                .createdAt(LocalDateTime.now())
                .creator(user01)
                .updatedAt(LocalDateTime.now())
                .updater(user02)
                .build();
        stockTableJpaRepository.save(stockOther);

        // act
        var stocks = stockRepository.getStocks();

        // assertion
        assertTrue(stocks.size() == 2);
    }

    @Test
    void getStock_SetupOnly_Success() {
        // arrange
        String stockId = StockAId;

        // act
        var entity = stockRepository.getStock(stockId);

        // assertion
        assertNotNull(entity);
        assertTrue(entity.DisplayName().name().equals(StockADisplayName));

    }

    @Test
    void getStock_AddItem_Success() {
        // arrange
        String itemId = UUID.randomUUID().toString();
        var itemC = StockItemTable.builder()
                .itemId(itemId)
                .itemName("追加商品")
                .amount(5)
                .costPrice(1000)
                .description("テスト用商品Bです")
                .build();
        stockItemTableJpaRepository.save(itemC);

        var wareHouse = wareHouseTableJpaRepository.findById(wareHouseId).get();
        wareHouse.getStockItems().add(itemC);
        wareHouseTableJpaRepository.save(wareHouse);

        itemC.setWareHouseTable(wareHouse);
        stockItemTableJpaRepository.save(itemC);

        var stockEntity = stockTableJpaRepository.findById(StockAId).get();
        stockEntity.getStockItems().add(itemC);
        stockTableJpaRepository.save(stockEntity);

        // act
        var entity = stockRepository.getStock(StockAId);

        // assertion
        assertNotNull(entity);
        assertTrue(entity.StockItems().size() == 3);
        assertTrue(entity.StockAmount() == 35);
    }

}