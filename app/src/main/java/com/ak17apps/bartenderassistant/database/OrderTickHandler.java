package com.ak17apps.bartenderassistant.database;

import com.ak17apps.bartenderassistant.dao.ComponentDao;
import com.ak17apps.bartenderassistant.dao.CompositeItemDao;
import com.ak17apps.bartenderassistant.dao.ItemDao;
import com.ak17apps.bartenderassistant.dao.OrderDao;
import com.ak17apps.bartenderassistant.dao.SellingAmountDao;
import com.ak17apps.bartenderassistant.dao.UnitExchangeDao;
import com.ak17apps.bartenderassistant.entity.Component;
import com.ak17apps.bartenderassistant.entity.CompositeItem;
import com.ak17apps.bartenderassistant.entity.Item;
import com.ak17apps.bartenderassistant.entity.Order;
import com.ak17apps.bartenderassistant.entity.SellingAmount;
import com.ak17apps.bartenderassistant.entity.UnitExchange;
import com.ak17apps.bartenderassistant.utils.OrderableTypes;

import java.util.List;

public class OrderTickHandler {
    public static void tickOrder(Order order, SellingAmountDao sellingAmountDao, ItemDao itemDao, OrderDao orderDao, UnitExchangeDao unitExchangeDao, CompositeItemDao compositeItemDao, ComponentDao componentDao){
        if(order.getOrderableType().equals(OrderableTypes.SELLING_AMOUNT.name())){
            SellingAmount sellingAmount = sellingAmountDao.getSellingAmountById(order.getOrderableId());
            Item item = itemDao.getItemById(sellingAmount.getItemId());

            if(sellingAmount.getUnitId() == item.getPackagingUnitId()){
                item.setStoredAmount(item.getStoredAmount() - sellingAmount.getAmount() * order.getQuantity());
                itemDao.update(item);
                orderDao.delete(order);
            }else{
                List<UnitExchange> unitExchangeList = unitExchangeDao.getUnitExchangeByFromUnitIdAndToUnitId(sellingAmount.getUnitId(), item.getPackagingUnitId());
                if(unitExchangeList != null && !unitExchangeList.isEmpty()){
                    UnitExchange unitExchange = unitExchangeList.get(0);
                    float rate = unitExchange.getToValue() / unitExchange.getFromValue();
                    item.setStoredAmount(item.getStoredAmount() - sellingAmount.getAmount() * rate * order.getQuantity());
                    itemDao.update(item);
                    orderDao.delete(order);
                }else{
                    unitExchangeList = unitExchangeDao.getUnitExchangeByFromUnitIdAndToUnitId(item.getPackagingUnitId(), sellingAmount.getUnitId());
                    if(unitExchangeList != null && !unitExchangeList.isEmpty()){
                        UnitExchange unitExchange = unitExchangeList.get(0);
                        float rate = unitExchange.getFromValue() / unitExchange.getToValue();
                        item.setStoredAmount(item.getStoredAmount() - sellingAmount.getAmount() * rate * order.getQuantity());
                        itemDao.update(item);
                        orderDao.delete(order);
                    }
                }
            }
        }else{
            CompositeItem compositeItem = compositeItemDao.getCompositeItemById(order.getOrderableId());
            List<Component> components = componentDao.getComponentsByCompositeItemId(compositeItem.getId());
            for(Component component : components){
                Item item = itemDao.getItemById(component.getItemId());

                if(component.getUnitId() == item.getPackagingUnitId()){
                    item.setStoredAmount(item.getStoredAmount() - component.getAmount() * order.getQuantity());
                    itemDao.update(item);
                    orderDao.delete(order);
                }else{
                    List<UnitExchange> unitExchangeList = unitExchangeDao.getUnitExchangeByFromUnitIdAndToUnitId(component.getUnitId(), item.getPackagingUnitId());
                    if(unitExchangeList != null && !unitExchangeList.isEmpty()){
                        UnitExchange unitExchange = unitExchangeList.get(0);
                        float rate = unitExchange.getToValue() / unitExchange.getFromValue();
                        item.setStoredAmount(item.getStoredAmount() - component.getAmount() * rate * order.getQuantity());
                        itemDao.update(item);
                        orderDao.delete(order);
                    }else{
                        unitExchangeList = unitExchangeDao.getUnitExchangeByFromUnitIdAndToUnitId(item.getPackagingUnitId(), component.getUnitId());
                        if(unitExchangeList != null && !unitExchangeList.isEmpty()){
                            UnitExchange unitExchange = unitExchangeList.get(0);
                            float rate = unitExchange.getFromValue() / unitExchange.getToValue();
                            item.setStoredAmount(item.getStoredAmount() - component.getAmount() * rate * order.getQuantity());
                            itemDao.update(item);
                            orderDao.delete(order);
                        }
                    }
                }
            }
        }
    }
}
