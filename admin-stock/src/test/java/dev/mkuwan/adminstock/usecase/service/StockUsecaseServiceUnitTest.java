package dev.mkuwan.adminstock.usecase.service;

import dev.mkuwan.adminstock.domain.event.EventType;
import dev.mkuwan.adminstock.domain.event.StockEvent;
import dev.mkuwan.adminstock.domain.model.StockModel;
import dev.mkuwan.adminstock.domain.repository.IStockRepository;
import dev.mkuwan.adminstock.domain.valueobject.DisplayName;
import dev.mkuwan.adminstock.domain.valueobject.StockItem;
import dev.mkuwan.adminstock.domain.valueobject.StockUser;
import dev.mkuwan.adminstock.domain.valueobject.WareHouse;
import dev.mkuwan.adminstock.usecase.dto.StockDto;
import dev.mkuwan.adminstock.usecase.event.StockEventListener;
import dev.mkuwan.adminstock.usecase.event.StockEventPublisher;
import dev.mkuwan.adminstock.domain.event.StockEventValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class StockUsecaseServiceUnitTest {

    @Mock
    private IStockRepository stockRepositoryMock;

    @Autowired
    private StockEventPublisher stockEventPublisher;


    @MockBean
    private StockEventListener stockEventListenerMock;

    @Autowired
    private IStockUsecaseService stockUsecaseService;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp(){
        autoCloseable = MockitoAnnotations.openMocks(this);

    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    private StockModel stubModelById(String stockId){
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
        return new StockModel(stockId,
                new DisplayName("表示用商品名A"),1800, 3, 3, "テストです",
                stockItems, "SN11223344", true, LocalDateTime.now(),creator,LocalDateTime.now(),updater);
    }

    /**
     * イベントの動作確認
     * 3回のイベント発生でリスナーが3回受信する
     */
    @Test
    void stockEventPublish_listenerGetEventOneTime() {
        // arrange
        var event = new StockEvent(this,
                new StockEventValue(UUID.randomUUID().toString(), "テスト商品",
                        1000, 2,3, 500, "説明"), EventType.Updated);

        // act
        stockEventPublisher.publishStockEvent(event);
        stockEventPublisher.publishStockEvent(event);
        stockEventPublisher.publishStockEvent(event);

        // assertion
        verify(stockEventListenerMock, times(3)).onApplicationEvent(event);

    }


    /**
     * イベントクラスをリスナーが受信する
     * 1回のものでしか確認できないので別テストとしています
     */
    @Test
    void stockEventListenerGetEvent_GetValueClass(){
        // arrange
        var event = new StockEvent(this,
                new StockEventValue(UUID.randomUUID().toString(), "テスト商品2",
                        100, 2,5, 5000, "説明2"), EventType.Updated);

        // act
        stockEventPublisher.publishStockEvent(event);

        // assertion
        verify(stockEventListenerMock).onApplicationEvent(any(StockEvent.class));
    }

     /**
     * 正常系
     * StockUsecaseServiceのgetStocksのUnit Test
     * Repositoryはモックにしている
     */
    @Test
    void getStocks_Success(){
        // arrange
        List<StockModel> stockModels = new ArrayList<>();
        var stockIdA = UUID.randomUUID().toString();
        var stockA = stubModelById(stockIdA);
        var stockIdB = UUID.randomUUID().toString();
        var stockB = stubModelById(stockIdB);
        stockModels.add(stockA);
        stockModels.add(stockB);

        var mockedStockUsecaseService = new StockUsecaseService(stockEventPublisher, stockRepositoryMock);
        when(stockRepositoryMock.getStocks()).thenReturn(stockModels);

        // act
        List<StockDto> stockDtoList = mockedStockUsecaseService.getStocks();

        // assertion
        assertTrue(stockDtoList.size() == 2);
    }

    /**
     * 在庫リスト取得　
     * DBに値がないため空となる
     */
    @Test
    void getStocks_ReturnEmpty(){
        // arrange
        var mockedStockUsecaseService = new StockUsecaseService(stockEventPublisher, stockRepositoryMock);
        when(stockRepositoryMock.getStocks()).thenReturn(null);

        // act
        List<StockDto> stockDtoList = mockedStockUsecaseService.getStocks();

        // assertion
        assertTrue(stockDtoList.size() == 0);
    }

    /**
     * 在庫リスト取得　1商品だけが戻る
     */
    @Test
    void getStocks_ReturnOneItem(){
        // arrange
        var mockedStockUsecaseService = new StockUsecaseService(stockEventPublisher, stockRepositoryMock);
        List<StockModel> stockModels = new ArrayList<>();
        List<StockItem> stockItems = new ArrayList<>();
        WareHouse wareHouse = new WareHouse(UUID.randomUUID().toString(), "倉庫A");
        StockUser userA = new StockUser(UUID.randomUUID().toString(), "userA");
        StockUser userB = new StockUser(UUID.randomUUID().toString(), "userB");
        stockItems.add(new StockItem(UUID.randomUUID().toString(),
                "商品1", 10, 1500, LocalDate.now(),"","",
                wareHouse, LocalDateTime.now(), userA, LocalDateTime.now(), userB));
        stockModels.add(new StockModel(UUID.randomUUID().toString(),
                new DisplayName("testItem"),
                3000, 10, 5, "説明",
                stockItems, "SERIAL001", true,
                LocalDateTime.now(), userA, LocalDateTime.now(), userB));
        when(stockRepositoryMock.getStocks()).thenReturn(stockModels);

        // act
        List<StockDto> stockDtoList = mockedStockUsecaseService.getStocks();

        // assertion
        assertTrue(stockDtoList.size() == 1);
    }

    /**
     * 単品在庫取得
     */
    @Test
    void getStock_Success(){
        // arrange
        List<StockModel> stockModels = new ArrayList<>();
        var stockIdA = UUID.randomUUID().toString();
        var stockA = stubModelById(stockIdA);
        var stockIdB = UUID.randomUUID().toString();
        var stockB = stubModelById(stockIdB);
        stockModels.add(stockA);
        stockModels.add(stockB);

        var mockedStockUsecaseService = new StockUsecaseService(stockEventPublisher, stockRepositoryMock);
        when(stockRepositoryMock.getStock(stockIdA)).thenReturn(stockModels.get(0));

        // act
        var stockDto = mockedStockUsecaseService.getStock(stockIdA);

        // assertion
        assertTrue(stockDto != null);
        verify(stockRepositoryMock, times(1)).getStock(stockIdA);

        // assertion by AssertJ
        assertThat(stockDto.getStockId()).isEqualTo(stockIdA);
    }

    /**
     * 単品在庫取得　
     * DBに値がないため空となる
     */
    @Test
    void getStock_ReturnEmpty(){
        // arrange
        var mockedStockUsecaseService = new StockUsecaseService(stockEventPublisher, stockRepositoryMock);
        when(stockRepositoryMock.getStock("testId")).thenReturn(null);

        // act
        var stockDto = mockedStockUsecaseService.getStock("testId");

        // assertion
        assertNull(stockDto);
        // by AssertJ
        assertThat(stockDto).isNull();
    }

    /**
     * 正常系
     * 価格修正　成功
     * 在庫リスト取得 -> 1つの商品を選択 -> 価格修正
     */
    @Test
    void determineSalesPrice_Success() {
        // arrange
        var mockedStockUsecaseService = new StockUsecaseService(stockEventPublisher, stockRepositoryMock);
        List<StockModel> stockModels = new ArrayList<>();
        List<StockItem> stockItems = new ArrayList<>();
        WareHouse wareHouse = new WareHouse(UUID.randomUUID().toString(), "倉庫A");
        StockUser userA = new StockUser(UUID.randomUUID().toString(), "userA");
        StockUser userB = new StockUser(UUID.randomUUID().toString(), "userB");
        stockItems.add(new StockItem(UUID.randomUUID().toString(),
                "商品1", 10, 1500, LocalDate.now(),"","",
                wareHouse, LocalDateTime.now(), userA, LocalDateTime.now(), userB));
        var stockId = UUID.randomUUID().toString();
        stockModels.add(new StockModel(stockId,
                new DisplayName("testItem"),
                3000, 10, 5, "説明",
                stockItems, "SERIAL001", true,
                LocalDateTime.now(), userA, LocalDateTime.now(), userB));
        when(stockRepositoryMock.getStocks()).thenReturn(stockModels);
        when(stockRepositoryMock.getStock(stockId)).thenReturn(stockModels.get(0));

        // act
        List<StockDto> stockDtoList = mockedStockUsecaseService.getStocks();
        var selectedStock = stockDtoList
                .stream()
                .filter(x -> x.getStockId().equals(stockId))
                .findFirst()
                .get();
        var rePrice = 4000;
        var selectedForRePriceStockDto = mockedStockUsecaseService.getStock(selectedStock.getStockId());

        selectedForRePriceStockDto.setSalesPrice(rePrice);
        var rePricedStockDto = mockedStockUsecaseService.determineSalesPrice(selectedStock.getStockId(), rePrice);

        // assert
        assertThat(rePricedStockDto.getSalesPrice()).isEqualTo(rePrice);

    }

    /**
     * 異常系
     * 価格修正: 1.5倍未満
     */
    @Test
    void determineSalesPriceLessThanAverageCostPrice_ThrowError() {
        // arrange
        var mockedStockUsecaseService = new StockUsecaseService(stockEventPublisher, stockRepositoryMock);
        List<StockModel> stockModels = new ArrayList<>();
        List<StockItem> stockItems = new ArrayList<>();
        WareHouse wareHouse = new WareHouse(UUID.randomUUID().toString(), "倉庫A");
        StockUser userA = new StockUser(UUID.randomUUID().toString(), "userA");
        StockUser userB = new StockUser(UUID.randomUUID().toString(), "userB");
        stockItems.add(new StockItem(UUID.randomUUID().toString(),
                "商品1", 10, 1500, LocalDate.now(),"","",
                wareHouse, LocalDateTime.now(), userA, LocalDateTime.now(), userB));
        var stockId = UUID.randomUUID().toString();
        stockModels.add(new StockModel(stockId,
                new DisplayName("testItem"),
                3000, 10, 5, "説明",
                stockItems, "SERIAL001", true,
                LocalDateTime.now(), userA, LocalDateTime.now(), userB));
        when(stockRepositoryMock.getStocks()).thenReturn(stockModels);
        when(stockRepositoryMock.getStock(stockId)).thenReturn(stockModels.get(0));

        // act
        List<StockDto> stockDtoList = mockedStockUsecaseService.getStocks();
        var selectedStock = stockDtoList
                .stream()
                .filter(x -> x.getStockId().equals(stockId))
                .findFirst()
                .get();
        var rePrice = 2249;
        var selectedForRePriceStockDto = mockedStockUsecaseService.getStock(selectedStock.getStockId());

        selectedForRePriceStockDto.setSalesPrice(rePrice);

        Throwable thrown = catchThrowable(() -> mockedStockUsecaseService.determineSalesPrice(selectedStock.getStockId(), rePrice));

        // assert
        then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("販売価格は平均仕入れ額1500円の1.5倍以上としてください");

    }

    /**
     * 異常系
     * 価格修正: 3倍より大きい
     */
    @Test
    void determineSalesPriceOverThanAverageCostPrice_ThrowError() {
        // arrange
        var mockedStockUsecaseService = new StockUsecaseService(stockEventPublisher, stockRepositoryMock);
        List<StockModel> stockModels = new ArrayList<>();
        List<StockItem> stockItems = new ArrayList<>();
        WareHouse wareHouse = new WareHouse(UUID.randomUUID().toString(), "倉庫A");
        StockUser userA = new StockUser(UUID.randomUUID().toString(), "userA");
        StockUser userB = new StockUser(UUID.randomUUID().toString(), "userB");
        stockItems.add(new StockItem(UUID.randomUUID().toString(),
                "商品1", 10, 1500, LocalDate.now(),"","",
                wareHouse, LocalDateTime.now(), userA, LocalDateTime.now(), userB));
        var stockId = UUID.randomUUID().toString();
        stockModels.add(new StockModel(stockId,
                new DisplayName("testItem"),
                3000, 10, 5, "説明",
                stockItems, "SERIAL001", true,
                LocalDateTime.now(), userA, LocalDateTime.now(), userB));
        when(stockRepositoryMock.getStocks()).thenReturn(stockModels);
        when(stockRepositoryMock.getStock(stockId)).thenReturn(stockModels.get(0));

        // act
        List<StockDto> stockDtoList = mockedStockUsecaseService.getStocks();
        var selectedStock = stockDtoList
                .stream()
                .filter(x -> x.getStockId().equals(stockId))
                .findFirst()
                .get();
        var rePrice = 4501;
        var selectedForRePriceStockDto = mockedStockUsecaseService.getStock(selectedStock.getStockId());

        selectedForRePriceStockDto.setSalesPrice(rePrice);

        Throwable thrown = catchThrowable(() -> mockedStockUsecaseService.determineSalesPrice(selectedStock.getStockId(), rePrice));

        // assert
        then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("販売価格は平均仕入れ額1500円の3.0倍以下としてください");

    }
}