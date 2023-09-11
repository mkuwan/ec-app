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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class StockUsecaseServiceTest {

    @Mock
    private IStockRepository stockRepositoryMock;

    @Autowired
    private StockEventPublisher stockEventPublisher;


    @MockBean
    private StockEventListener stockEventListenerMock;

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
     * StockUsecaseServiceのgetStockのUnit Test
     * Repositoryはモックにしている
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

        var stockUsecaseService = new StockUsecaseService(stockEventPublisher, stockRepositoryMock);
        when(stockRepositoryMock.getStocks()).thenReturn(stockModels);

        // act
        List<StockDto> stockDtoList = stockUsecaseService.getStocks();

        // assertion
        assertTrue(stockDtoList.size() == 2);
    }

    @Test
    void getStock_ReturnEmpty(){
        // arrange
        var stockUsecaseService = new StockUsecaseService(stockEventPublisher, stockRepositoryMock);
        when(stockRepositoryMock.getStocks()).thenReturn(null);

        // act
        List<StockDto> stockDtoList = stockUsecaseService.getStocks();

        // assertion
        assertTrue(stockDtoList.size() == 0);
    }

}