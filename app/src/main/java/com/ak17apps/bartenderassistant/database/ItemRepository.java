package com.ak17apps.bartenderassistant.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.dao.ItemDao;
import com.ak17apps.bartenderassistant.dao.UnitDao;
import com.ak17apps.bartenderassistant.entity.Item;

import java.util.List;

public class ItemRepository {
    private ItemDao itemDao;
    private LiveData<List<Item>> allItems;
    private LiveData<List<Item>> itemsByName;

    public ItemRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        itemDao = db.itemDao();
        allItems = itemDao.getAllItem();
    }

    public void setItemName(String itemName){
        itemsByName = itemDao.getItemsByName(itemName);
    }

    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public LiveData<List<Item>> getItemsByName() {
        return itemsByName;
    }

    public synchronized void insert (Item item) {
        new insertAsyncTask(itemDao).execute(item);
    }

    public void delete(Item item) {
        new DeleteItemAsyncTask(itemDao).execute(item);
    }

    public void update(Item item) {
        new UpdateItemAsyncTask(itemDao).execute(item);
    }

    public void deleteAll(){
        new DeleteAllAsyncTask(itemDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDao itemDao;

        DeleteAllAsyncTask(ItemDao dao) {
            itemDao = dao;
        }

        @Override
        protected Void doInBackground(final Item... items) {
            itemDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Item, Void, Void> {

        private ItemDao itemDao;

        insertAsyncTask(ItemDao dao) {
            itemDao = dao;
        }

        @Override
        protected Void doInBackground(final Item... params) {
            itemDao.insert(params[0]);
            return null;
        }
    }

    private static class UpdateItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDao itemDao;

        private UpdateItemAsyncTask(ItemDao itemDAO) {
            this.itemDao = itemDAO;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.update(items[0]);
            return null;
        }
    }

    private static class DeleteItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDao itemDao;

        private DeleteItemAsyncTask(ItemDao itemDAO) {
            this.itemDao = itemDAO;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.delete(items[0]);
            return null;
        }
    }
}
