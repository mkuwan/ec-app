package dev.mkuwan.adminstock.usecase.service;

import dev.mkuwan.adminstock.domain.event.StockEvent;
import dev.mkuwan.adminstock.domain.valueobject.StockItem;
import dev.mkuwan.adminstock.domain.valueobject.WareHouse;
import dev.mkuwan.adminstock.usecase.event.StockEventListener;
import dev.mkuwan.adminstock.usecase.event.StockEventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class StockUsecaseServiceTest {

    @Autowired
    private IStockUsecaseService stockUsecaseService;

    @Autowired
    private StockEventPublisher stockEventPublisher;

    @MockBean
    private StockEventListener stockEventListenerMock;


    @Test
    void eventTest() {
        // arrange
        var item = new StockItem(UUID.randomUUID().toString(), "テスト商品", 1, 1000,
                LocalDate.now(), "理由", "説明",
                new WareHouse(UUID.randomUUID().toString(), "倉庫"));
        var event = new StockEvent(item);

        // act
        stockEventPublisher.publishEvent(event);

        // assertion
        verify(stockEventListenerMock, times(1)).onApplicationEvent(event);
    }
}