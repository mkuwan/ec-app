package dev.mkuwan.adminstock.domain.event;

public record StockUpdatedEventValue(EventType eventType,
                                     String stockId,
                                     String displayName,
                                     long salesPrice,
                                     int stockAmount,
                                     int purchaseLimit,
                                     float averageCostPrice,
                                     String description) {

}
