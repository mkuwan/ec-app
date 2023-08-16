package dev.mkuwan.adminstock.domain.event;

import org.springframework.context.ApplicationEvent;

public class StockEvent extends ApplicationEvent implements IDomainEvent {
    public StockEvent(Object source) {
        super(source);
    }
}
