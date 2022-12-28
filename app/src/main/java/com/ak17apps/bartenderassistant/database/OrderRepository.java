package com.ak17apps.bartenderassistant.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.dao.ComponentDao;
import com.ak17apps.bartenderassistant.dao.CompositeItemDao;
import com.ak17apps.bartenderassistant.dao.ItemDao;
import com.ak17apps.bartenderassistant.dao.OrderDao;
import com.ak17apps.bartenderassistant.dao.SellingAmountDao;
import com.ak17apps.bartenderassistant.dao.UnitExchangeDao;
import com.ak17apps.bartenderassistant.entity.Order;

import java.util.List;

public class OrderRepository {
    private OrderDao orderDao;
    private SellingAmountDao sellingAmountDao;
    private CompositeItemDao compositeItemDao;
    private ItemDao itemDao;
    private UnitExchangeDao unitExchangeDao;
    private ComponentDao componentDao;
    private LiveData<List<Order>> ordersOfBoard;
    private LiveData<List<Order>> allOrders;
    private LiveData<Float> sumPriceOfBoard;

    public OrderRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        orderDao = db.orderDao();
        sellingAmountDao = db.sellingAmountDao();
        compositeItemDao = db.compositeItemDao();
        itemDao = db.itemDao();
        unitExchangeDao = db.unitExchangeDao();
        componentDao = db.componentDao();
        allOrders = orderDao.getAllOrders();
    }

    public void setBoardId(int boardId){
        ordersOfBoard = orderDao.getOrdersOfBoard(boardId);
        sumPriceOfBoard = orderDao.getSumPriceOfBoard(boardId);
    }

    public LiveData<List<Order>> getAllOrders() {
        return allOrders;
    }

    public LiveData<List<Order>> getOrdersOfBoard() {
        return ordersOfBoard;
    }

    public LiveData<Float> getSumPriceOfBoard() {
        return sumPriceOfBoard;
    }
    
    public synchronized void insert (Order order) {
        new insertAsyncTask(orderDao).execute(order);
    }

    public void delete(Order order) {
        new DeleteOrderAsyncTask(orderDao).execute(order);
    }

    public void sign(Order order){
        new SignOrderAsyncTask(orderDao).execute(order);
    }

    public void tick(Order order) {
        new TickOrderAsyncTask(orderDao, sellingAmountDao, compositeItemDao, itemDao, unitExchangeDao, componentDao).execute(order);
    }

    public void update(Order order) {
        new UpdateOrderAsyncTask(orderDao).execute(order);
    }

    public void deleteAll(){
        new DeleteAllAsyncTask(orderDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Order, Void, Void> {
        private OrderDao orderDao;

        DeleteAllAsyncTask(OrderDao dao) {
            orderDao = dao;
        }

        @Override
        protected Void doInBackground(final Order... orders) {
            orderDao.deleteAll();
            return null;
        }
    }

    private static class getOrdersOfBoardAsyncTask extends AsyncTask<Integer, Void, LiveData<List<Order>>> {
        private OrderDao orderDao;

        getOrdersOfBoardAsyncTask(OrderDao dao) {
            orderDao = dao;
        }

        @Override
        protected LiveData<List<Order>> doInBackground(Integer... boardId) {
            return orderDao.getOrdersOfBoard(boardId[0]);
        }
    }

    private static class insertAsyncTask extends AsyncTask<Order, Void, Void> {

        private OrderDao orderDao;

        insertAsyncTask(OrderDao dao) {
            orderDao = dao;
        }

        @Override
        protected Void doInBackground(final Order... params) {
            orderDao.insert(params[0]);
            return null;
        }
    }

    private static class UpdateOrderAsyncTask extends AsyncTask<Order, Void, Void> {
        private OrderDao orderDao;

        private UpdateOrderAsyncTask(OrderDao OrderDao) {
            this.orderDao = OrderDao;
        }

        @Override
        protected Void doInBackground(Order... orders) {
            orderDao.update(orders[0]);
            return null;
        }
    }

    private static class DeleteOrderAsyncTask extends AsyncTask<Order, Void, Void> {
        private OrderDao orderDao;

        private DeleteOrderAsyncTask(OrderDao OrderDao) {
            this.orderDao = OrderDao;
        }

        @Override
        protected Void doInBackground(Order... orders) {
            orderDao.delete(orders[0]);
            return null;
        }
    }

    private static class TickOrderAsyncTask extends AsyncTask<Order, Void, Void> {
        private OrderDao orderDao;
        private SellingAmountDao sellingAmountDao;
        private CompositeItemDao compositeItemDao;
        private ItemDao itemDao;
        private UnitExchangeDao unitExchangeDao;
        private ComponentDao componentDao;

        private TickOrderAsyncTask(OrderDao orderDao, SellingAmountDao sellingAmountDao, CompositeItemDao compositeItemDao, ItemDao itemDao, UnitExchangeDao unitExchangeDao, ComponentDao componentDao) {
            this.orderDao = orderDao;
            this.sellingAmountDao = sellingAmountDao;
            this.compositeItemDao = compositeItemDao;
            this.itemDao = itemDao;
            this.unitExchangeDao = unitExchangeDao;
            this.componentDao = componentDao;
        }

        @Override
        protected Void doInBackground(Order... orders) {
            OrderTickHandler.tickOrder(orders[0], sellingAmountDao, itemDao, orderDao, unitExchangeDao, compositeItemDao, componentDao);
            return null;
        }
    }

    private static class SignOrderAsyncTask extends AsyncTask<Order, Void, Void> {
        private OrderDao orderDao;

        private SignOrderAsyncTask(OrderDao orderDao) {
            this.orderDao = orderDao;
        }

        @Override
        protected Void doInBackground(Order... orders) {
            Order order = orders[0];
            order.setSigned(!order.isSigned());
            orderDao.update(order);
            return null;
        }
    }
}
