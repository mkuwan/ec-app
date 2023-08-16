package dev.mkuwan.adminstock.usecase.service;


import dev.mkuwan.adminstock.domain.event.StockEvent;
import dev.mkuwan.adminstock.domain.model.StockModel;
import dev.mkuwan.adminstock.domain.valueobject.StockItem;
import dev.mkuwan.adminstock.domain.valueobject.WareHouse;
import dev.mkuwan.adminstock.usecase.event.StockEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class StockUsecaseService implements IStockUsecaseService{
    private final StockEventPublisher publisher;

    public StockUsecaseService(StockEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void eventTest(StockItem item){

        var event = new StockEvent(item);
        publisher.publishEvent(event);
    }
}
