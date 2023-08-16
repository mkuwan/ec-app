package dev.mkuwan.adminstock.usecase.event;

import dev.mkuwan.adminstock.domain.event.StockEvent;
import dev.mkuwan.adminstock.domain.valueobject.StockItem;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StockEventListener implements ApplicationListener<StockEvent> {
    @Override
    public void onApplicationEvent(StockEvent event) {
        if(event.getSource().getClass().equals(StockItem.class)){
            var item = (StockItem)event.getSource();
            System.out.println(item.itemName());
        }
        System.out.println("StockEventを受信しました");
    }
}
