package com.ak17apps.bartenderassistant.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.dao.CompositeItemDao;
import com.ak17apps.bartenderassistant.entity.CompositeItem;

import java.util.List;

public class CompositeItemRepository {
    private CompositeItemDao compositeItemDao;
    private LiveData<List<CompositeItem>> allCompositeItems;
    private LiveData<List<CompositeItem>> compositeItemsOfCategory;
    private LiveData<Integer> numberOfCompositeItemsOfCategory;

    public CompositeItemRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        compositeItemDao = db.compositeItemDao();
        allCompositeItems = compositeItemDao.getAllCompositeItems();
    }

    public void setCategoryId(int categoryId){
        numberOfCompositeItemsOfCategory = compositeItemDao.getNumberOfCompositeItemsOfCategory(categoryId);
        compositeItemsOfCategory = compositeItemDao.getCompositeItemsOfCategory(categoryId);
    }

    public LiveData<List<CompositeItem>> getAllCompositeItems() {
        return allCompositeItems;
    }

    public LiveData<List<CompositeItem>> getCompositeItemsOfCategory(){
        return compositeItemsOfCategory;
    }

    public LiveData<Integer> getNumberOfCompositeItemsOfCategory() {
        return numberOfCompositeItemsOfCategory;
    }

    public synchronized void insert (CompositeItem compositeItem) {
        new insertAsyncTask(compositeItemDao).execute(compositeItem);
    }

    public void delete(CompositeItem compositeItem) {
        new DeleteCompositeItemAsyncTask(compositeItemDao).execute(compositeItem);
    }

    public void update(CompositeItem compositeItem) {
        new UpdateCompositeItemAsyncTask(compositeItemDao).execute(compositeItem);
    }

    public void deleteAll(){
        new DeleteAllAsyncTask(compositeItemDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<CompositeItem, Void, Void> {
        private CompositeItemDao compositeItemDao;

        DeleteAllAsyncTask(CompositeItemDao dao) {
            compositeItemDao = dao;
        }

        @Override
        protected Void doInBackground(final CompositeItem... compositeItems) {
            compositeItemDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<CompositeItem, Void, Void> {
        private CompositeItemDao compositeItemDao;

        insertAsyncTask(CompositeItemDao dao) {
            compositeItemDao = dao;
        }

        @Override
        protected Void doInBackground(final CompositeItem... compositeItem) {
            compositeItemDao.insert(compositeItem[0]);
            return null;
        }
    }

    private static class UpdateCompositeItemAsyncTask extends AsyncTask<CompositeItem, Void, Void> {
        private CompositeItemDao compositeItemDao;

        private UpdateCompositeItemAsyncTask(CompositeItemDao compositeItem) {
            this.compositeItemDao = compositeItem;
        }

        @Override
        protected Void doInBackground(CompositeItem... compositeItem) {
            compositeItemDao.update(compositeItem[0]);
            return null;
        }
    }

    private static class DeleteCompositeItemAsyncTask extends AsyncTask<CompositeItem, Void, Void> {
        private CompositeItemDao compositeItemDao;

        private DeleteCompositeItemAsyncTask(CompositeItemDao compositeItemDao) {
            this.compositeItemDao = compositeItemDao;
        }

        @Override
        protected Void doInBackground(CompositeItem... compositeItem) {
            compositeItemDao.delete(compositeItem[0]);
            return null;
        }
    }
}
