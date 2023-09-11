package dev.mkuwan.adminstock.domain.event;

public record StockEventValue(String stockId,
                              String displayName,
                              long salesPrice,
                              int stockAmount,
                              int purchaseLimit,
                              float averageCostPrice,
                              String description) {

}
