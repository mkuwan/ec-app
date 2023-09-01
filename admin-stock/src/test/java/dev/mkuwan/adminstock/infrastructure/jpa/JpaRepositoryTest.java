package dev.mkuwan.adminstock.infrastructure.jpa;

import dev.mkuwan.adminstock.infrastructure.entity.StockItemTable;
import dev.mkuwan.adminstock.infrastructure.entity.StockTable;
import dev.mkuwan.adminstock.infrastructure.entity.WareHouseTable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * `@DataJpaTest:=エンティティとレポジトリ間だけでのテストをサポートしてくれる。
 * `              @Entityと@RepositoryのついたクラスApplicationContextへロードする。
 * `@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
 * とすることでアプリケーションで設定されているDBを使うことができる。
 */
@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaRepositoryTest {

    @Autowired
    private IWareHouseTableJpaRepository wareHouseTableJpaRepository;

    @Autowired
    private IStockItemTableJpaRepository stockItemTableJpaRepository;

    @Autowired
    private IStockTableJpaRepository stockTableJpaRepository;


    @Test
    void addWareHouse_created(){
        // arrange
        var wareHouse = WareHouseTable.builder()
                .wareHouseId(UUID.randomUUID().toString())
                .wareHouseName("テスト倉庫B")
                .build();
        wareHouseTableJpaRepository.save(wareHouse);

        // act
        var result = wareHouseTableJpaRepository.findAll();

        // assert
        assertTrue(result.size() >= 1);
        var houseB = result.stream()
                .filter(x -> x.getWareHouseName().equals("テスト倉庫B"))
                .findFirst().get();
        assertTrue(houseB.getStockItems() == null);
    }

    @Test
    void getWareHose_HouseAHasTwoStockItems(){
        // arrange
        var item01Id = UUID.randomUUID().toString();
        var item01 = StockItemTable.builder()
                .itemId(item01Id)
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

        // act
        var wareHouseResult = wareHouseTableJpaRepository
                .findWareHouseTablesByWareHouseName("テスト倉庫A").orElseGet(null)
                .stream().findFirst().orElseGet(null);

        var itemB = wareHouseResult.getStockItems()
                .stream().filter(x -> x.getItemName().equals("テスト商品B"))
                .findFirst().get();

        var dbItems = stockItemTableJpaRepository
                .findById(item01Id).get();

        // assert
        assertTrue(wareHouseResult.getStockItems().size() == 2);
        assertThat(itemB.getAmount()).isEqualTo(5);
        assertTrue(dbItems.getItemName().equals("テスト商品A"));
    }

    @Test
    void getStock_WareHouseAHasTwoStockItems(){
        // arrange
        var item01Id = UUID.randomUUID().toString();
        var item01 = StockItemTable.builder()
                .itemId(item01Id)
                .itemName("テスト商品A")
                .amount(10)
                .costPrice(1000)
                .description("テスト用商品Aです")
                .build();


        var item02 = StockItemTable.builder()
                .itemId(UUID.randomUUID().toString())
                .itemName("テスト商品B")
                .amount(5)
                .costPrice(1200)
                .description("テスト用商品Bです")
                .build();


        var items = new HashSet<StockItemTable>();
        items.add(item01);
        items.add(item02);
        var wareHouse = WareHouseTable.builder()
                .wareHouseId(UUID.randomUUID().toString())
                .wareHouseName("テスト倉庫A")
                .StockItems(items)
                .build();

        stockItemTableJpaRepository.save(item01);
        stockItemTableJpaRepository.save(item02);
        wareHouseTableJpaRepository.save(wareHouse);

        item01.setWareHouseTable(wareHouse);
        stockItemTableJpaRepository.save(item01);
        item02.setWareHouseTable(wareHouse);
        stockItemTableJpaRepository.save(item02);

        var stockId = UUID.randomUUID().toString();
        var stock = StockTable.builder()
                .stockId(stockId)
                .stockItems(items)
                .stockAmount(15)
                .itemSerialNumber("ABC_SERIAL")
                .canOrder(true)
                .displayName("しょうひん")
                .averageCostPrice(1067)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        stockTableJpaRepository.save(stock);

        // act
        var stockResult = stockTableJpaRepository
                .findById(stockId).get();

        var displayName = stockResult.getDisplayName();

        var itemA = stockResult.getStockItems()
                .stream()
                .filter(x -> x.getItemId().equals(item01Id))
                .findFirst().get();

        var wareHouseName = itemA.getWareHouseTable().getWareHouseName();

        // assert
        assertTrue(displayName.equals("しょうひん"));
        assertThat(itemA.getAmount()).isEqualTo(10);
        assertTrue(wareHouseName.equals("テスト倉庫A"));
    }
}