package com.ak17apps.bartenderassistant.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.dao.SellingAmountDao;
import com.ak17apps.bartenderassistant.entity.SellingAmount;

import java.util.List;

public class SellingAmountRepository {
    private SellingAmountDao sellingAmountDao;
    private LiveData<List<SellingAmount>> allSellingAmounts;
    private LiveData<List<SellingAmount>> allSellingAmountsOfCategory;

    public SellingAmountRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        sellingAmountDao = db.sellingAmountDao();
        allSellingAmounts = sellingAmountDao.getAllSellingAmounts();
    }

    public void setCategoryId(int categoryId){
        allSellingAmountsOfCategory = sellingAmountDao.getSellingAmountsOfCategoryId(categoryId);
    }

    public LiveData<List<SellingAmount>> getSellingAmountsOfCategory() {
        return allSellingAmountsOfCategory;
    }

    public LiveData<List<SellingAmount>> getAllSellingAmounts() {
        return allSellingAmounts;
    }

    public synchronized void insert (SellingAmount sellingAmount) {
        new insertAsyncTask(sellingAmountDao).execute(sellingAmount);
    }

    public void delete(SellingAmount sellingAmount) {
        new DeleteSellingAmountAsyncTask(sellingAmountDao).execute(sellingAmount);
    }

    public void update(SellingAmount sellingAmount) {
        new UpdateSellingAmountAsyncTask(sellingAmountDao).execute(sellingAmount);
    }

    public void deleteAll(){
        new DeleteAllAsyncTask(sellingAmountDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<SellingAmount, Void, Void> {
        private SellingAmountDao sellingAmountDao;

        DeleteAllAsyncTask(SellingAmountDao dao) {
            sellingAmountDao = dao;
        }

        @Override
        protected Void doInBackground(final SellingAmount... units) {
            sellingAmountDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<SellingAmount, Void, Void> {

        private SellingAmountDao sellingAmountDao;

        insertAsyncTask(SellingAmountDao dao) {
            sellingAmountDao = dao;
        }

        @Override
        protected Void doInBackground(final SellingAmount... params) {
            sellingAmountDao.insert(params[0]);
            return null;
        }
    }

    private static class UpdateSellingAmountAsyncTask extends AsyncTask<SellingAmount, Void, Void> {
        private SellingAmountDao sellingAmountDao;

        private UpdateSellingAmountAsyncTask(SellingAmountDao SellingAmountDao) {
            this.sellingAmountDao = SellingAmountDao;
        }

        @Override
        protected Void doInBackground(SellingAmount... sellingAmounts) {
            sellingAmountDao.update(sellingAmounts[0]);
            return null;
        }
    }

    private static class DeleteSellingAmountAsyncTask extends AsyncTask<SellingAmount, Void, Void> {
        private SellingAmountDao sellingAmountDao;

        private DeleteSellingAmountAsyncTask(SellingAmountDao SellingAmountDao) {
            this.sellingAmountDao = SellingAmountDao;
        }

        @Override
        protected Void doInBackground(SellingAmount... sellingAmounts) {
            sellingAmountDao.delete(sellingAmounts[0]);
            return null;
        }
    }
}
