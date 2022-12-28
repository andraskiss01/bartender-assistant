package com.ak17apps.bartenderassistant.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.database.OrderRepository;
import com.ak17apps.bartenderassistant.entity.Order;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {
    private OrderRepository orderRepository;

    public OrderViewModel(Application application) {
        super(application);
        orderRepository = new OrderRepository(application);
    }

    public void setBoardId(int boardId){
        orderRepository.setBoardId(boardId);
    }
    public LiveData<List<Order>> getOrdersOfBoard() { return orderRepository.getOrdersOfBoard(); }
    public LiveData<Float> getSumPrice() {
        return orderRepository.getSumPriceOfBoard();
    }
    public void insert(Order order) { orderRepository.insert(order); }
    public void update(Order order) { orderRepository.update(order); }
    public void delete(Order order) { orderRepository.delete(order); }
    public void tick(Order order) { orderRepository.tick(order); }
    public void sign(Order order) { orderRepository.sign(order); }
}