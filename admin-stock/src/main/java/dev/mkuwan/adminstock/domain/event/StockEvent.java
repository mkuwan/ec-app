package dev.mkuwan.adminstock.domain.event;

import org.springframework.context.ApplicationEvent;

/**
 * 在庫更新イベント
 */
public class StockEvent extends ApplicationEvent implements IDomainEvent {
    private final StockUpdatedEventValue eventValue;
    private final EventType eventType;



    public StockEvent(Object source, StockUpdatedEventValue eventValue) {
        super(source);
        this.eventValue = eventValue;
        this.eventType = EventType.Updated;
    }


    public StockUpdatedEventValue getEventValue() {
        return eventValue;
    }

    public EventType getEventType() {
        return eventType;
    }
}
