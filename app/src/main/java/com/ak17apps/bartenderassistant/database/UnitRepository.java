package com.ak17apps.bartenderassistant.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.dao.UnitDao;
import com.ak17apps.bartenderassistant.entity.Unit;

import java.util.List;

public class UnitRepository {
    private UnitDao unitDao;
    private LiveData<List<Unit>> allUnits;

    public UnitRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        unitDao = db.unitDao();
        allUnits = unitDao.getAllUnits();
    }

    public LiveData<List<Unit>> getAllUnits() {
        return allUnits;
    }

    public String getUnitNameById(int id){
        return unitDao.getUnitById(id).getName();
    }

    public synchronized void insert (Unit unit) { new insertAsyncTask(unitDao).execute(unit); }

    public void delete(Unit unit) {
        new DeleteUnitAsyncTask(unitDao).execute(unit);
    }

    public void update(Unit unit) {
        new UpdateUnitAsyncTask(unitDao).execute(unit);
    }

    public void deleteAll(){
        new DeleteAllAsyncTask(unitDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Unit, Void, Void> {
        private UnitDao unitDao;

        DeleteAllAsyncTask(UnitDao dao) {
            unitDao = dao;
        }

        @Override
        protected Void doInBackground(final Unit... units) {
            unitDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Unit, Void, Void> {
        private UnitDao unitDao;

        insertAsyncTask(UnitDao dao) {
            unitDao = dao;
        }

        @Override
        protected Void doInBackground(final Unit... units) {
            unitDao.insert(units[0]);
            return null;
        }
    }

    private static class UpdateUnitAsyncTask extends AsyncTask<Unit, Void, Void> {
        private UnitDao unitDao;

        private UpdateUnitAsyncTask(UnitDao UnitDao) {
            this.unitDao = UnitDao;
        }

        @Override
        protected Void doInBackground(Unit... units) {
            unitDao.update(units[0]);
            return null;
        }
    }

    private static class DeleteUnitAsyncTask extends AsyncTask<Unit, Void, Void> {
        private UnitDao unitDao;

        private DeleteUnitAsyncTask(UnitDao UnitDao) {
            this.unitDao = UnitDao;
        }

        @Override
        protected Void doInBackground(Unit... units) {
            unitDao.delete(units[0]);
            return null;
        }
    }
}
