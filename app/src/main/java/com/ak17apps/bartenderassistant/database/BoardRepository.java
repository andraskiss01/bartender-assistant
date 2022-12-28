package com.ak17apps.bartenderassistant.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.dao.BoardDao;
import com.ak17apps.bartenderassistant.dao.ComponentDao;
import com.ak17apps.bartenderassistant.dao.CompositeItemDao;
import com.ak17apps.bartenderassistant.dao.ItemDao;
import com.ak17apps.bartenderassistant.dao.OrderDao;
import com.ak17apps.bartenderassistant.dao.SellingAmountDao;
import com.ak17apps.bartenderassistant.dao.UnitExchangeDao;
import com.ak17apps.bartenderassistant.entity.Board;
import com.ak17apps.bartenderassistant.entity.Order;

import java.util.List;

public class BoardRepository {
    private BoardDao boardDao;
    private OrderDao orderDao;
    private SellingAmountDao sellingAmountDao;
    private CompositeItemDao compositeItemDao;
    private ItemDao itemDao;
    private UnitExchangeDao unitExchangeDao;
    private ComponentDao componentDao;
    private LiveData<List<Board>> mAllBoards;

    public BoardRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        boardDao = db.boardDao();
        orderDao = db.orderDao();
        sellingAmountDao = db.sellingAmountDao();
        compositeItemDao = db.compositeItemDao();
        itemDao = db.itemDao();
        unitExchangeDao = db.unitExchangeDao();
        componentDao = db.componentDao();
        mAllBoards = boardDao.getAllBoards();
    }

    public LiveData<List<Board>> getAllBoards() {
        return mAllBoards;
    }

    public synchronized void insert (Board board) {
        new insertAsyncTask(boardDao).execute(board);
    }

    public void delete(Board board) {
        new DeleteBoardAsyncTask(boardDao).execute(board);
    }

    public void tick(Board board) {
        new TickBoardAsyncTask(boardDao, orderDao, sellingAmountDao, compositeItemDao, itemDao, unitExchangeDao, componentDao).execute(board);
    }

    public void update(Board board) {
        new UpdateBoardAsyncTask(boardDao).execute(board);
    }

    public void deleteAll(){
        new DeleteAllAsyncTask(boardDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Board, Void, Void> {
        private BoardDao boardDao;

        DeleteAllAsyncTask(BoardDao dao) {
            boardDao = dao;
        }

        @Override
        protected Void doInBackground(final Board... boards) {
            boardDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Board, Void, Void> {
        private BoardDao boardDao;

        insertAsyncTask(BoardDao dao) {
            boardDao = dao;
        }

        @Override
        protected Void doInBackground(final Board... boards) {
            boardDao.insert(boards[0]);
            return null;
        }
    }

    private static class UpdateBoardAsyncTask extends AsyncTask<Board, Void, Void> {
        private BoardDao boardDao;

        private UpdateBoardAsyncTask(BoardDao boardDao) {
            this.boardDao = boardDao;
        }

        @Override
        protected Void doInBackground(Board... boards) {
            boardDao.update(boards[0]);
            return null;
        }
    }

    private static class DeleteBoardAsyncTask extends AsyncTask<Board, Void, Void> {
        private BoardDao boardDao;

        private DeleteBoardAsyncTask(BoardDao boardDao) {
            this.boardDao = boardDao;
        }

        @Override
        protected Void doInBackground(Board... boards) {
            boardDao.delete(boards[0]);
            return null;
        }
    }

    private static class TickBoardAsyncTask extends AsyncTask<Board, Void, Void> {
        private BoardDao boardDao;
        private OrderDao orderDao;
        private SellingAmountDao sellingAmountDao;
        private CompositeItemDao compositeItemDao;
        private ItemDao itemDao;
        private UnitExchangeDao unitExchangeDao;
        private ComponentDao componentDao;

        private TickBoardAsyncTask(BoardDao boardDao, OrderDao orderDao, SellingAmountDao sellingAmountDao, CompositeItemDao compositeItemDao, ItemDao itemDao, UnitExchangeDao unitExchangeDao, ComponentDao componentDao) {
            this.boardDao = boardDao;
            this.orderDao = orderDao;
            this.sellingAmountDao = sellingAmountDao;
            this.compositeItemDao = compositeItemDao;
            this.itemDao = itemDao;
            this.unitExchangeDao = unitExchangeDao;
            this.componentDao = componentDao;
        }

        @Override
        protected Void doInBackground(Board... boards) {
            Board board = boards[0];
            List<Order> orders = orderDao.getOrderListOfBoard(board.getId());
            if(orders != null && !orders.isEmpty()){
                for(Order o : orders){
                    OrderTickHandler.tickOrder(o, sellingAmountDao, itemDao, orderDao, unitExchangeDao, compositeItemDao, componentDao);
                }
            }
            boardDao.delete(board);

            return null;
        }
    }
}
