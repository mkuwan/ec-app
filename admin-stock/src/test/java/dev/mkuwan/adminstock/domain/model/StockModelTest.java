package dev.mkuwan.adminstock.domain.model;

import dev.mkuwan.adminstock.domain.valueobject.DisplayName;
import dev.mkuwan.adminstock.domain.valueobject.StockItem;
import dev.mkuwan.adminstock.domain.valueobject.StockUser;
import dev.mkuwan.adminstock.domain.valueobject.WareHouse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockModelTest {

    /**
     * 正常系
     * StockModel作成
     */
    @Test
    void createStockModel_created(){
        // arrange
        var wareHouseId = UUID.randomUUID().toString();
        var itemId01 = UUID.randomUUID().toString();
        var itemId02 = UUID.randomUUID().toString();
        var itemId03 = UUID.randomUUID().toString();
        var userId01 = UUID.randomUUID().toString();
        var userId02 = UUID.randomUUID().toString();

        StockUser creator = new StockUser(userId01, "在庫作成者");
        StockUser updater = new StockUser(userId02, "在庫管理者");
        WareHouse wareHouse = new WareHouse(wareHouseId, "倉庫1");

        List<StockItem> stockItems = new ArrayList<>();
        StockItem stockItemA01 = new StockItem(itemId01, "商品A", 3, 800,
                LocalDate.of(2023, 8, 1), null, "テスト仕入れ1", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA02 = new StockItem(itemId02, "商品A", 3, 690,
                LocalDate.of(2023, 8, 15), null, "テスト仕入れ2", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA03 = new StockItem(itemId03, "商品aa", 4, 720,
                LocalDate.of(2023, 9, 1), null, "テスト仕入れ3", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        stockItems.add(stockItemA01);
        stockItems.add(stockItemA02);
        stockItems.add(stockItemA03);

        // act
        // assertion
        assertDoesNotThrow(() -> new StockModel(UUID.randomUUID().toString(),
                new DisplayName("表示用商品名A"),1300, 10, 3, "テストです",
                stockItems, "SN11223344", true, LocalDateTime.now(),creator,LocalDateTime.now(),updater));
    }

    /**
     * 正常系 閾値
     * 販売価格が平均仕入れ価格の3倍
     */
    @Test
    void createStockModel_SalesPriceMaximumThresholdAverageCost(){
        // arrange
        var wareHouseId = UUID.randomUUID().toString();
        var itemId01 = UUID.randomUUID().toString();
        var itemId02 = UUID.randomUUID().toString();
        var itemId03 = UUID.randomUUID().toString();
        var userId01 = UUID.randomUUID().toString();
        var userId02 = UUID.randomUUID().toString();

        StockUser creator = new StockUser(userId01, "在庫作成者");
        StockUser updater = new StockUser(userId02, "在庫管理者");
        WareHouse wareHouse = new WareHouse(wareHouseId, "倉庫1");

        List<StockItem> stockItems = new ArrayList<>();
        StockItem stockItemA01 = new StockItem(itemId01, "商品A", 3, 1000,
                LocalDate.of(2023, 8, 1), null, "テスト仕入れ1", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA02 = new StockItem(itemId02, "商品A", 3, 1000,
                LocalDate.of(2023, 8, 15), null, "テスト仕入れ2", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA03 = new StockItem(itemId03, "商品aa", 4, 1000,
                LocalDate.of(2023, 9, 1), null, "テスト仕入れ3", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        stockItems.add(stockItemA01);
        stockItems.add(stockItemA02);
        stockItems.add(stockItemA03);

        // act
        var stockModel = new StockModel(UUID.randomUUID().toString(),
                new DisplayName("表示用商品名A"),1800, 10, 3, "テストです",
                stockItems, "SN11223344", true, LocalDateTime.now(),creator,LocalDateTime.now(),updater);

        // assertion
        assertDoesNotThrow(() -> stockModel.determineSalesPrice(3000));

    }

    /**
     * 正常系　閾値
     * 販売価格が平均仕入れ価格1.5倍
     */
    @Test
    void createStockModel_SalesPriceMinimumThresholdAverageCost(){
        // arrange
        var wareHouseId = UUID.randomUUID().toString();
        var itemId01 = UUID.randomUUID().toString();
        var itemId02 = UUID.randomUUID().toString();
        var itemId03 = UUID.randomUUID().toString();
        var userId01 = UUID.randomUUID().toString();
        var userId02 = UUID.randomUUID().toString();

        StockUser creator = new StockUser(userId01, "在庫作成者");
        StockUser updater = new StockUser(userId02, "在庫管理者");
        WareHouse wareHouse = new WareHouse(wareHouseId, "倉庫1");

        List<StockItem> stockItems = new ArrayList<>();
        StockItem stockItemA01 = new StockItem(itemId01, "商品A", 3, 1000,
                LocalDate.of(2023, 8, 1), null, "テスト仕入れ1", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA02 = new StockItem(itemId02, "商品A", 3, 1000,
                LocalDate.of(2023, 8, 15), null, "テスト仕入れ2", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA03 = new StockItem(itemId03, "商品aa", 4, 1000,
                LocalDate.of(2023, 9, 1), null, "テスト仕入れ3", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        stockItems.add(stockItemA01);
        stockItems.add(stockItemA02);
        stockItems.add(stockItemA03);

        // act
        var stockModel = new StockModel(UUID.randomUUID().toString(),
                new DisplayName("表示用商品名A"),1800, 10, 3, "テストです",
                stockItems, "SN11223344", true, LocalDateTime.now(),creator,LocalDateTime.now(),updater);

        // assertion
        assertDoesNotThrow(() -> stockModel.determineSalesPrice(1500));
    }

    /**
     * 正常系 閾値
     * 販売価格が平均仕入れ価格を四捨五入(切り捨て)の3倍
     */
    @Test
    void createStockModel_SalesPriceMaximumThresholdRoundDownAverageCost(){
        // arrange
        var wareHouseId = UUID.randomUUID().toString();
        var itemId01 = UUID.randomUUID().toString();
        var itemId02 = UUID.randomUUID().toString();
        var itemId03 = UUID.randomUUID().toString();
        var userId01 = UUID.randomUUID().toString();
        var userId02 = UUID.randomUUID().toString();

        StockUser creator = new StockUser(userId01, "在庫作成者");
        StockUser updater = new StockUser(userId02, "在庫管理者");
        WareHouse wareHouse = new WareHouse(wareHouseId, "倉庫1");

        List<StockItem> stockItems = new ArrayList<>();
        StockItem stockItemA01 = new StockItem(itemId01, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 1), null, "テスト仕入れ1", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA02 = new StockItem(itemId02, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 15), null, "テスト仕入れ2", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA03 = new StockItem(itemId03, "商品aa", 1, 800,
                LocalDate.of(2023, 9, 1), null, "テスト仕入れ3", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        stockItems.add(stockItemA01);
        stockItems.add(stockItemA02);
        stockItems.add(stockItemA03);

        // act
        var stockModel = new StockModel(UUID.randomUUID().toString(),
                new DisplayName("表示用商品名A"),1800, 3, 3, "テストです",
                stockItems, "SN11223344", true, LocalDateTime.now(),creator,LocalDateTime.now(),updater);
        var average = stockModel.AverageCostPrice();
        // assertion
        System.out.println("平均仕入れ額" + average); // 933.333333...
        assertTrue(average == 933f);
        // 3倍 2799
        assertDoesNotThrow(() -> stockModel.determineSalesPrice(2799));
    }

    /**
     * 正常系 閾値
     * 販売価格が平均仕入れ価格を四捨五入(切り上げ)の3倍
     */
    @Test
    void createStockModel_SalesPriceMaximumThresholdRoundDUpAverageCost(){
        // arrange
        var wareHouseId = UUID.randomUUID().toString();
        var itemId01 = UUID.randomUUID().toString();
        var itemId02 = UUID.randomUUID().toString();
        var itemId03 = UUID.randomUUID().toString();
        var userId01 = UUID.randomUUID().toString();
        var userId02 = UUID.randomUUID().toString();

        StockUser creator = new StockUser(userId01, "在庫作成者");
        StockUser updater = new StockUser(userId02, "在庫管理者");
        WareHouse wareHouse = new WareHouse(wareHouseId, "倉庫1");

        List<StockItem> stockItems = new ArrayList<>();
        StockItem stockItemA01 = new StockItem(itemId01, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 1), null, "テスト仕入れ1", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA02 = new StockItem(itemId02, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 15), null, "テスト仕入れ2", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA03 = new StockItem(itemId03, "商品aa", 1, 900,
                LocalDate.of(2023, 9, 1), null, "テスト仕入れ3", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        stockItems.add(stockItemA01);
        stockItems.add(stockItemA02);
        stockItems.add(stockItemA03);

        // act
        var stockModel = new StockModel(UUID.randomUUID().toString(),
                new DisplayName("表示用商品名A"),1800, 3, 3, "テストです",
                stockItems, "SN11223344", true, LocalDateTime.now(),creator,LocalDateTime.now(),updater);
        var average = stockModel.AverageCostPrice();
        // assertion
        System.out.println("平均仕入れ額" + average); // 966.6666....
        assertTrue(average == 967f);
        // 3倍 2901
        assertDoesNotThrow(() -> stockModel.determineSalesPrice(2901));
    }

    /**
     * 正常系　閾値
     * 販売価格が平均仕入れ価格を四捨五入(切り捨て)の1.5倍
     */
    @Test
    void createStockModel_SalesPriceMinimumThresholdRoundDownAverageCost(){
        // arrange
        var wareHouseId = UUID.randomUUID().toString();
        var itemId01 = UUID.randomUUID().toString();
        var itemId02 = UUID.randomUUID().toString();
        var itemId03 = UUID.randomUUID().toString();
        var userId01 = UUID.randomUUID().toString();
        var userId02 = UUID.randomUUID().toString();

        StockUser creator = new StockUser(userId01, "在庫作成者");
        StockUser updater = new StockUser(userId02, "在庫管理者");
        WareHouse wareHouse = new WareHouse(wareHouseId, "倉庫1");

        List<StockItem> stockItems = new ArrayList<>();
        StockItem stockItemA01 = new StockItem(itemId01, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 1), null, "テスト仕入れ1", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA02 = new StockItem(itemId02, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 15), null, "テスト仕入れ2", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA03 = new StockItem(itemId03, "商品aa", 1, 800,
                LocalDate.of(2023, 9, 1), null, "テスト仕入れ3", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        stockItems.add(stockItemA01);
        stockItems.add(stockItemA02);
        stockItems.add(stockItemA03);

        // act
        var stockModel = new StockModel(UUID.randomUUID().toString(),
                new DisplayName("表示用商品名A"),1800, 3, 3, "テストです",
                stockItems, "SN11223344", true, LocalDateTime.now(),creator,LocalDateTime.now(),updater);
        var average = stockModel.AverageCostPrice();
        // assertion
        System.out.println("平均仕入れ額" + average); // 933.333333...
        assertTrue(average == 933f);
        // 1.5倍 1399.5
        assertDoesNotThrow(() -> stockModel.determineSalesPrice(1400));
    }

    /**
     * 正常系　閾値
     * 販売価格が平均仕入れ価格を四捨五入(切り上げ)の1.5倍
     */
    @Test
    void createStockModel_SalesPriceMinimumThresholdRoundDUpAverageCost(){
        // arrange
        var wareHouseId = UUID.randomUUID().toString();
        var itemId01 = UUID.randomUUID().toString();
        var itemId02 = UUID.randomUUID().toString();
        var itemId03 = UUID.randomUUID().toString();
        var userId01 = UUID.randomUUID().toString();
        var userId02 = UUID.randomUUID().toString();

        StockUser creator = new StockUser(userId01, "在庫作成者");
        StockUser updater = new StockUser(userId02, "在庫管理者");
        WareHouse wareHouse = new WareHouse(wareHouseId, "倉庫1");

        List<StockItem> stockItems = new ArrayList<>();
        StockItem stockItemA01 = new StockItem(itemId01, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 1), null, "テスト仕入れ1", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA02 = new StockItem(itemId02, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 15), null, "テスト仕入れ2", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA03 = new StockItem(itemId03, "商品aa", 1, 900,
                LocalDate.of(2023, 9, 1), null, "テスト仕入れ3", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        stockItems.add(stockItemA01);
        stockItems.add(stockItemA02);
        stockItems.add(stockItemA03);

        // act
        var stockModel = new StockModel(UUID.randomUUID().toString(),
                new DisplayName("表示用商品名A"),1800, 3, 3, "テストです",
                stockItems, "SN11223344", true, LocalDateTime.now(),creator,LocalDateTime.now(),updater);
        var average = stockModel.AverageCostPrice();
        // assertion
        System.out.println("平均仕入れ額" + average); // 966.6666....
        assertTrue(average == 967f);
        // 1.5倍 1450.5
        assertDoesNotThrow(() -> stockModel.determineSalesPrice(1451));
    }

    /**
     * 異常系
     * 販売価格が平均仕入れ価格の1.5倍未満
     */
    @Test
    void createStockModel_SaleCostLessThanAverageCost_ThrowError(){
        // arrange
        var wareHouseId = UUID.randomUUID().toString();
        var itemId01 = UUID.randomUUID().toString();
        var itemId02 = UUID.randomUUID().toString();
        var itemId03 = UUID.randomUUID().toString();
        var userId01 = UUID.randomUUID().toString();
        var userId02 = UUID.randomUUID().toString();

        StockUser creator = new StockUser(userId01, "在庫作成者");
        StockUser updater = new StockUser(userId02, "在庫管理者");
        WareHouse wareHouse = new WareHouse(wareHouseId, "倉庫1");

        List<StockItem> stockItems = new ArrayList<>();
        StockItem stockItemA01 = new StockItem(itemId01, "商品A", 3, 1000,
                LocalDate.of(2023, 8, 1), null, "テスト仕入れ1", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA02 = new StockItem(itemId02, "商品A", 3, 1000,
                LocalDate.of(2023, 8, 15), null, "テスト仕入れ2", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA03 = new StockItem(itemId03, "商品aa", 4, 1000,
                LocalDate.of(2023, 9, 1), null, "テスト仕入れ3", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        stockItems.add(stockItemA01);
        stockItems.add(stockItemA02);
        stockItems.add(stockItemA03);

        // act
        var stockModel = new StockModel(UUID.randomUUID().toString(),
                new DisplayName("表示用商品名A"),1800, 10, 3, "テストです",
                stockItems, "SN11223344", true, LocalDateTime.now(),creator,LocalDateTime.now(),updater);
        // assertion
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> stockModel.determineSalesPrice(800));

        String expectedMessage = "販売価格は平均仕入れ額1000円の1.5倍以上としてください";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.equals(expectedMessage));
    }

    /**
     * 異常系
     * 販売価格が平均仕入れ価格の3倍より大きい
     */
    @Test
    void createStockModel_SalesPriceOverThanAverageCost_ThrowError(){
        // arrange
        var wareHouseId = UUID.randomUUID().toString();
        var itemId01 = UUID.randomUUID().toString();
        var itemId02 = UUID.randomUUID().toString();
        var itemId03 = UUID.randomUUID().toString();
        var userId01 = UUID.randomUUID().toString();
        var userId02 = UUID.randomUUID().toString();

        StockUser creator = new StockUser(userId01, "在庫作成者");
        StockUser updater = new StockUser(userId02, "在庫管理者");
        WareHouse wareHouse = new WareHouse(wareHouseId, "倉庫1");

        List<StockItem> stockItems = new ArrayList<>();
        StockItem stockItemA01 = new StockItem(itemId01, "商品A", 3, 1000,
                LocalDate.of(2023, 8, 1), null, "テスト仕入れ1", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA02 = new StockItem(itemId02, "商品A", 3, 1000,
                LocalDate.of(2023, 8, 15), null, "テスト仕入れ2", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA03 = new StockItem(itemId03, "商品aa", 4, 1000,
                LocalDate.of(2023, 9, 1), null, "テスト仕入れ3", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        stockItems.add(stockItemA01);
        stockItems.add(stockItemA02);
        stockItems.add(stockItemA03);

        // act
        var stockModel = new StockModel(UUID.randomUUID().toString(),
                new DisplayName("表示用商品名A"),1800, 10, 3, "テストです",
                stockItems, "SN11223344", true, LocalDateTime.now(),creator,LocalDateTime.now(),updater);
        // assertion
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> stockModel.determineSalesPrice(4000));

        String expectedMessage = "販売価格は平均仕入れ額1000円の3.0倍以下としてください";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.equals(expectedMessage));
    }

    /**
     * 異常系　閾値
     * 販売価格が平均仕入れ価格を四捨五入(切り捨て)の3倍より大きい
     */
    @Test
    void createStockModel_SalesPriceMaximumThresholdRoundDownAverageCost_ThrowError(){
        // arrange
        var wareHouseId = UUID.randomUUID().toString();
        var itemId01 = UUID.randomUUID().toString();
        var itemId02 = UUID.randomUUID().toString();
        var itemId03 = UUID.randomUUID().toString();
        var userId01 = UUID.randomUUID().toString();
        var userId02 = UUID.randomUUID().toString();

        StockUser creator = new StockUser(userId01, "在庫作成者");
        StockUser updater = new StockUser(userId02, "在庫管理者");
        WareHouse wareHouse = new WareHouse(wareHouseId, "倉庫1");

        List<StockItem> stockItems = new ArrayList<>();
        StockItem stockItemA01 = new StockItem(itemId01, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 1), null, "テスト仕入れ1", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA02 = new StockItem(itemId02, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 15), null, "テスト仕入れ2", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA03 = new StockItem(itemId03, "商品aa", 1, 800,
                LocalDate.of(2023, 9, 1), null, "テスト仕入れ3", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        stockItems.add(stockItemA01);
        stockItems.add(stockItemA02);
        stockItems.add(stockItemA03);

        // act
        var stockModel = new StockModel(UUID.randomUUID().toString(),
                new DisplayName("表示用商品名A"),1800, 3, 3, "テストです",
                stockItems, "SN11223344", true, LocalDateTime.now(),creator,LocalDateTime.now(),updater);
        var average = stockModel.AverageCostPrice();
        // assertion
        System.out.println("平均仕入れ額" + average); // 933.333333...
        assertTrue(average == 933f);
        // 3倍 2799
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> stockModel.determineSalesPrice(2800));

        String expectedMessage = "販売価格は平均仕入れ額933円の3.0倍以下としてください";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.equals(expectedMessage));
    }

    /**
     * 異常系　閾値
     * 販売価格が平均仕入れ価格を四捨五入(切り上げ)の3倍より大きい
     */
    @Test
    void createStockModel_SalesPriceMaximumThresholdRoundDUpAverageCost_ThrowError(){
        // arrange
        var wareHouseId = UUID.randomUUID().toString();
        var itemId01 = UUID.randomUUID().toString();
        var itemId02 = UUID.randomUUID().toString();
        var itemId03 = UUID.randomUUID().toString();
        var userId01 = UUID.randomUUID().toString();
        var userId02 = UUID.randomUUID().toString();

        StockUser creator = new StockUser(userId01, "在庫作成者");
        StockUser updater = new StockUser(userId02, "在庫管理者");
        WareHouse wareHouse = new WareHouse(wareHouseId, "倉庫1");

        List<StockItem> stockItems = new ArrayList<>();
        StockItem stockItemA01 = new StockItem(itemId01, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 1), null, "テスト仕入れ1", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA02 = new StockItem(itemId02, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 15), null, "テスト仕入れ2", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA03 = new StockItem(itemId03, "商品aa", 1, 900,
                LocalDate.of(2023, 9, 1), null, "テスト仕入れ3", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        stockItems.add(stockItemA01);
        stockItems.add(stockItemA02);
        stockItems.add(stockItemA03);

        // act
        var stockModel = new StockModel(UUID.randomUUID().toString(),
                new DisplayName("表示用商品名A"),1800, 3, 3, "テストです",
                stockItems, "SN11223344", true, LocalDateTime.now(),creator,LocalDateTime.now(),updater);
        var average = stockModel.AverageCostPrice();
        // assertion
        System.out.println("平均仕入れ額" + average); // 966.6666....
        assertTrue(average == 967f);
        // 3倍 2901
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> stockModel.determineSalesPrice(2902));

        String expectedMessage = "販売価格は平均仕入れ額967円の3.0倍以下としてください";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.equals(expectedMessage));
    }

    /**
     * 異常系
     * 販売価格が平均仕入れ価格を四捨五入(切り捨て)の1.5倍より小さい
     */
    @Test
    void createStockModel_SalesPriceMinimumThresholdRoundDownAverageCost_ThrowError(){
        // arrange
        var wareHouseId = UUID.randomUUID().toString();
        var itemId01 = UUID.randomUUID().toString();
        var itemId02 = UUID.randomUUID().toString();
        var itemId03 = UUID.randomUUID().toString();
        var userId01 = UUID.randomUUID().toString();
        var userId02 = UUID.randomUUID().toString();

        StockUser creator = new StockUser(userId01, "在庫作成者");
        StockUser updater = new StockUser(userId02, "在庫管理者");
        WareHouse wareHouse = new WareHouse(wareHouseId, "倉庫1");

        List<StockItem> stockItems = new ArrayList<>();
        StockItem stockItemA01 = new StockItem(itemId01, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 1), null, "テスト仕入れ1", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA02 = new StockItem(itemId02, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 15), null, "テスト仕入れ2", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA03 = new StockItem(itemId03, "商品aa", 1, 800,
                LocalDate.of(2023, 9, 1), null, "テスト仕入れ3", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        stockItems.add(stockItemA01);
        stockItems.add(stockItemA02);
        stockItems.add(stockItemA03);

        // act
        var stockModel = new StockModel(UUID.randomUUID().toString(),
                new DisplayName("表示用商品名A"),1800, 3, 3, "テストです",
                stockItems, "SN11223344", true, LocalDateTime.now(),creator,LocalDateTime.now(),updater);
        var average = stockModel.AverageCostPrice();
        // assertion
        System.out.println("平均仕入れ額" + average); // 933.333333...
        assertTrue(average == 933f);
        // 1.5倍 1399.5
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> stockModel.determineSalesPrice(1399));

        String expectedMessage = "販売価格は平均仕入れ額933円の1.5倍以上としてください";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.equals(expectedMessage));
    }

    /**
     * 異常系
     * 販売価格が平均仕入れ価格を四捨五入(切り上げ)の1.5倍より小さい
     */
    @Test
    void createStockModel_SalesPriceMinimumThresholdRoundDUpAverageCost_ThrowError(){
        // arrange
        var wareHouseId = UUID.randomUUID().toString();
        var itemId01 = UUID.randomUUID().toString();
        var itemId02 = UUID.randomUUID().toString();
        var itemId03 = UUID.randomUUID().toString();
        var userId01 = UUID.randomUUID().toString();
        var userId02 = UUID.randomUUID().toString();

        StockUser creator = new StockUser(userId01, "在庫作成者");
        StockUser updater = new StockUser(userId02, "在庫管理者");
        WareHouse wareHouse = new WareHouse(wareHouseId, "倉庫1");

        List<StockItem> stockItems = new ArrayList<>();
        StockItem stockItemA01 = new StockItem(itemId01, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 1), null, "テスト仕入れ1", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA02 = new StockItem(itemId02, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 15), null, "テスト仕入れ2", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        StockItem stockItemA03 = new StockItem(itemId03, "商品aa", 1, 900,
                LocalDate.of(2023, 9, 1), null, "テスト仕入れ3", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        stockItems.add(stockItemA01);
        stockItems.add(stockItemA02);
        stockItems.add(stockItemA03);

        // act
        var stockModel = new StockModel(UUID.randomUUID().toString(),
                new DisplayName("表示用商品名A"),1800, 3, 3, "テストです",
                stockItems, "SN11223344", true, LocalDateTime.now(),creator,LocalDateTime.now(),updater);
        var average = stockModel.AverageCostPrice();
        // assertion
        System.out.println("平均仕入れ額" + average); // 966.6666....
        assertTrue(average == 967f);
        // 1.5倍 1450.5
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> stockModel.determineSalesPrice(1450));

        String expectedMessage = "販売価格は平均仕入れ額967円の1.5倍以上としてください";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.equals(expectedMessage));
    }


    /**
     * 購入限度数　閾値
     * 最小数
     */
    @Test
    void amountThresholdMinimum(){
        // arrange
        var wareHouseId = UUID.randomUUID().toString();
        var itemId01 = UUID.randomUUID().toString();
        var userId01 = UUID.randomUUID().toString();
        var userId02 = UUID.randomUUID().toString();

        StockUser creator = new StockUser(userId01, "在庫作成者");
        StockUser updater = new StockUser(userId02, "在庫管理者");
        WareHouse wareHouse = new WareHouse(wareHouseId, "倉庫1");

        List<StockItem> stockItems = new ArrayList<>();
        StockItem stockItemA01 = new StockItem(itemId01, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 1), null, "テスト仕入れ1", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        stockItems.add(stockItemA01);

        // act
        var stockModel = new StockModel(UUID.randomUUID().toString(),
                new DisplayName("表示用商品名A"),1800, 3, 3, "テストです",
                stockItems, "SN11223344", true, LocalDateTime.now(),creator,LocalDateTime.now(),updater);

        // assertion
        assertDoesNotThrow(() -> stockModel.fixLimitedAmount(1));
    }

    /**
     * 異常系
     * 購入限度数が1未満
     */
    @Test
    void LimitedAmounかLessThanOne_ThrowError(){
        // arrange
        var wareHouseId = UUID.randomUUID().toString();
        var itemId01 = UUID.randomUUID().toString();
        var userId01 = UUID.randomUUID().toString();
        var userId02 = UUID.randomUUID().toString();

        StockUser creator = new StockUser(userId01, "在庫作成者");
        StockUser updater = new StockUser(userId02, "在庫管理者");
        WareHouse wareHouse = new WareHouse(wareHouseId, "倉庫1");

        List<StockItem> stockItems = new ArrayList<>();
        StockItem stockItemA01 = new StockItem(itemId01, "商品A", 1, 1000,
                LocalDate.of(2023, 8, 1), null, "テスト仕入れ1", wareHouse,
                LocalDateTime.now(), creator, LocalDateTime.now(), updater);
        stockItems.add(stockItemA01);

        // act
        var stockModel = new StockModel(UUID.randomUUID().toString(),
                new DisplayName("表示用商品名A"),1800, 3, 3, "テストです",
                stockItems, "SN11223344", true, LocalDateTime.now(),creator,LocalDateTime.now(),updater);

        // assertion
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> stockModel.fixLimitedAmount(0));

        String expectedMessage = "購入限度数は1以上としてください";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.equals(expectedMessage));
    }
}