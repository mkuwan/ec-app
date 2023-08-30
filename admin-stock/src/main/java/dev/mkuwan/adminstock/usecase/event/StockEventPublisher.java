package dev.mkuwan.adminstock.usecase.event;

import dev.mkuwan.adminstock.domain.event.IDomainEvent;
import dev.mkuwan.adminstock.domain.event.StockEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class StockEventPublisher {
    private final ApplicationEventPublisher publisher;

    public StockEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishStockEvent(IDomainEvent event){
        System.out.println("EventをPublishします");
        publisher.publishEvent(event);
    }
}
