package dev.mkuwan.adminstock.domain.valueobject;

import io.micrometer.common.util.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StockItemTest {

    private String stockItemId = UUID.randomUUID().toString();
    private String itemName;
    private int amount;
    private long costPrice;
    private LocalDate localDate = LocalDate.now();
    private String reason;
    private String description;
    private WareHouse wareHouse = new WareHouse(UUID.randomUUID().toString(), "テスト倉庫");
    private StockUser user01 = new StockUser(UUID.randomUUID().toString(), "作成者");;
    private StockUser user02 = new StockUser(UUID.randomUUID().toString(), "更新者");;

    @Test
    void amountIsLessThanZero_ThrowException(){
        // arrange
        itemName = "テストアイテム";
        costPrice = 0L;
        amount = -1;

        // act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            var item = new StockItem(stockItemId, itemName,
                    amount, costPrice, localDate, reason, description, wareHouse,
                    LocalDateTime.now(), user01, LocalDateTime.now(), user02);
        });

        // assertion
        String expectedMessage = "商品数を0より小さくすることはできません";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void amountIsThanZero_AssertionSucceeds(){
        // arrange
        itemName = "テストアイテム";
        costPrice = 1000L;
        amount = 1;

        // act
        var item = new StockItem(stockItemId, itemName,
                amount, costPrice, localDate, reason, description, wareHouse,
                LocalDateTime.now(), user01, LocalDateTime.now(), user02);

        // assertion
        assertEquals(amount, item.amount());
        assertDoesNotThrow(() -> new StockItem(stockItemId, itemName,
                amount, costPrice, localDate, reason, description, wareHouse,
                LocalDateTime.now(), user01, LocalDateTime.now(), user02));
    }

    @Test
    void costIsLessThanZero_ThrowException(){
        // arrange
        itemName = "テストアイテム";
        costPrice = -1L;
        amount = 1;

        // act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            var item = new StockItem(stockItemId, itemName,
                    amount, costPrice, localDate, reason, description, wareHouse,
                    LocalDateTime.now(), user01, LocalDateTime.now(), user02);
        });

        // assertion
        String expectedMessage = "仕入れ価格を0円より小さくすることはできません";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void costIsThanZero_AssertionSucceeds(){
        // arrange
        itemName = "テストアイテム";
        costPrice = 1000L;
        amount = 1;

        // act
        var item = new StockItem(stockItemId, itemName,
                amount, costPrice, localDate, reason, description, wareHouse,
                LocalDateTime.now(), user01, LocalDateTime.now(), user02);

        // assertion
        assertEquals(costPrice, item.costPrice());
        assertDoesNotThrow(() -> new StockItem(stockItemId, itemName,
                amount, costPrice, localDate, reason, description, wareHouse,
                LocalDateTime.now(), user01, LocalDateTime.now(), user02));
    }

    @Test
    void itemNameIsNull_ThrowException(){
        // arrange
        itemName = null;
        costPrice = 100L;
        amount = 1;

        // act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            var item = new StockItem(stockItemId, itemName,
                    amount, costPrice, localDate, reason, description, wareHouse,
                    LocalDateTime.now(), user01, LocalDateTime.now(), user02);
        });

        // assertion
        String expectedMessage = "商品名がありません";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void itemNameIsEmpty_ThrowException(){
        // arrange
        itemName = "";
        costPrice = 100L;
        amount = 1;

        // act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            var item = new StockItem(stockItemId, itemName,
                    amount, costPrice, localDate, reason, description, wareHouse,
                    LocalDateTime.now(), user01, LocalDateTime.now(), user02);
        });

        // assertion
        String expectedMessage = "商品名がありません";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void itemNameIsAnyStrings_AssertionSucceeds(){
        // arrange
        itemName = "テストアイテム";
        costPrice = 1000L;
        amount = 1;

        // act
        var item = new StockItem(stockItemId, itemName,
                amount, costPrice, localDate, reason, description, wareHouse,
                LocalDateTime.now(), user01, LocalDateTime.now(), user02);

        // assertion
        assertEquals(itemName, item.itemName());
        assertDoesNotThrow(() -> new StockItem(stockItemId, itemName,
                amount, costPrice, localDate, reason, description, wareHouse,
                LocalDateTime.now(), user01, LocalDateTime.now(), user02));
    }
}