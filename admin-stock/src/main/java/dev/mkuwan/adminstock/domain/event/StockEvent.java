package dev.mkuwan.adminstock.domain.event;

import org.springframework.context.ApplicationEvent;

/**
 * 在庫更新イベント
 */
public class StockEvent extends ApplicationEvent implements IDomainEvent {
    private final StockEventValue eventValue;
    private final EventType eventType;



    public StockEvent(Object source, StockEventValue eventValue, EventType eventType) {
        super(source);
        this.eventValue = eventValue;
        this.eventType = eventType;
    }


    public StockEventValue EventValue() {
        return eventValue;
    }

    public EventType EventType() {
        return eventType;
    }
}
