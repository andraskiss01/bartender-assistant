package com.ak17apps.bartenderassistant.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.dao.SupplyDao;
import com.ak17apps.bartenderassistant.entity.Supply;

import java.util.List;

public class SupplyRepository {
    private SupplyDao supplyDao;
    private LiveData<List<Supply>> allSupplies;

    public SupplyRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        supplyDao = db.supplyDao();
        allSupplies = supplyDao.getAllSupplies();
    }

    public LiveData<List<Supply>> getAllSupplies() {
        return allSupplies;
    }
    
    public synchronized void insert (Supply supply) {
        new insertAsyncTask(supplyDao).execute(supply);
    }

    public void delete(Supply supply) {
        new DeleteSupplyAsyncTask(supplyDao).execute(supply);
    }

    public void update(Supply supply) {
        new UpdateSupplyAsyncTask(supplyDao).execute(supply);
    }

    public void deleteAll(){
        new DeleteAllAsyncTask(supplyDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Supply, Void, Void> {
        private SupplyDao supplyDao;

        DeleteAllAsyncTask(SupplyDao dao) {
            supplyDao = dao;
        }

        @Override
        protected Void doInBackground(final Supply... supplies) {
            supplyDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Supply, Void, Void> {
        private SupplyDao supplyDao;

        insertAsyncTask(SupplyDao dao) {
            supplyDao = dao;
        }

        @Override
        protected Void doInBackground(final Supply... supplies) {
            supplyDao.insert(supplies[0]);
            return null;
        }
    }

    private static class UpdateSupplyAsyncTask extends AsyncTask<Supply, Void, Void> {
        private SupplyDao supplyDao;

        private UpdateSupplyAsyncTask(SupplyDao SupplyDao) {
            this.supplyDao = SupplyDao;
        }

        @Override
        protected Void doInBackground(Supply... supplies) {
            supplyDao.update(supplies[0]);
            return null;
        }
    }

    private static class DeleteSupplyAsyncTask extends AsyncTask<Supply, Void, Void> {
        private SupplyDao supplyDao;

        private DeleteSupplyAsyncTask(SupplyDao SupplyDao) {
            this.supplyDao = SupplyDao;
        }

        @Override
        protected Void doInBackground(Supply... supplies) {
            supplyDao.delete(supplies[0]);
            return null;
        }
    }
}
