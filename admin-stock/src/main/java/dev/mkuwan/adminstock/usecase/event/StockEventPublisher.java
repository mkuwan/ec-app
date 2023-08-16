package dev.mkuwan.adminstock.usecase.event;

import dev.mkuwan.adminstock.domain.event.IDomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class StockEventPublisher {
    private final ApplicationEventPublisher publisher;

    public StockEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishEvent(IDomainEvent event){
        publisher.publishEvent(event);
    }
}
