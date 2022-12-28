package com.ak17apps.bartenderassistant.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.dao.UnitExchangeDao;
import com.ak17apps.bartenderassistant.entity.UnitExchange;

import java.util.List;

public class UnitExchangeRepository {
    private UnitExchangeDao unitExchangeDao;
    private LiveData<List<UnitExchange>> allUnitExchanges;

    public UnitExchangeRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        unitExchangeDao = db.unitExchangeDao();
        allUnitExchanges = unitExchangeDao.getAllUnitExchanges();
    }

    public LiveData<List<UnitExchange>> getAllUnitExchanges() {
        return allUnitExchanges;
    }

    public synchronized void insert (UnitExchange unitExchange) { new insertAsyncTask(unitExchangeDao).execute(unitExchange); }

    public void delete(UnitExchange unitExchange) {
        new DeleteUnitAsyncTask(unitExchangeDao).execute(unitExchange);
    }

    public void update(UnitExchange unitExchange) {
        new UpdateUnitAsyncTask(unitExchangeDao).execute(unitExchange);
    }

    public void deleteAll(){
        new DeleteAllAsyncTask(unitExchangeDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<UnitExchange, Void, Void> {
        private UnitExchangeDao unitExchangeDao;

        DeleteAllAsyncTask(UnitExchangeDao unitExchangeDao) {
            this.unitExchangeDao = unitExchangeDao;
        }

        @Override
        protected Void doInBackground(final UnitExchange... unitExchanges) {
            unitExchangeDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<UnitExchange, Void, Void> {
        private UnitExchangeDao unitExchangeDao;

        insertAsyncTask(UnitExchangeDao unitExchangeDao) {
            this.unitExchangeDao = unitExchangeDao;
        }

        @Override
        protected Void doInBackground(final UnitExchange... unitExchanges) {
            unitExchangeDao.insert(unitExchanges[0]);
            return null;
        }
    }

    private static class UpdateUnitAsyncTask extends AsyncTask<UnitExchange, Void, Void> {
        private UnitExchangeDao unitExchangeDao;

        private UpdateUnitAsyncTask(UnitExchangeDao unitExchangeDao) {
            this.unitExchangeDao = unitExchangeDao;
        }

        @Override
        protected Void doInBackground(UnitExchange... unitExchanges) {
            unitExchangeDao.update(unitExchanges[0]);
            return null;
        }
    }

    private static class DeleteUnitAsyncTask extends AsyncTask<UnitExchange, Void, Void> {
        private UnitExchangeDao unitExchangeDao;

        private DeleteUnitAsyncTask(UnitExchangeDao unitExchangeDao) {
            this.unitExchangeDao = unitExchangeDao;
        }

        @Override
        protected Void doInBackground(UnitExchange... unitExchanges) {
            unitExchangeDao.delete(unitExchanges[0]);
            return null;
        }
    }
}
