package dev.mkuwan.adminstock.usecase.event;

import dev.mkuwan.adminstock.domain.event.StockEvent;
import dev.mkuwan.adminstock.domain.repository.IStockRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

@Component
public class StockEventListener implements ApplicationListener<StockEvent> {

    private final IStockRepository stockRepository;

    public StockEventListener(IStockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public void onApplicationEvent(StockEvent event) {
        System.out.println("Event Type is := " + event.getEventType().name());
        System.out.println("StockEventを受信しました: " + event.getEventValue().displayName());
    }


}
