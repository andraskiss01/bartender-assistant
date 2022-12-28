package com.ak17apps.bartenderassistant.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.dao.ComponentDao;
import com.ak17apps.bartenderassistant.entity.Component;

import java.util.List;

public class ComponentRepository {
    private ComponentDao componentDao;
    private LiveData<List<Component>> componentsByCompositeItemId;
    private LiveData<List<Component>> allComponents;

    public ComponentRepository(Application application, int componentId) {
        AppDatabase db = AppDatabase.getDatabase(application);
        componentDao = db.componentDao();
        componentsByCompositeItemId = componentDao.getComponentByCompositeItemId(componentId);
    }

    public ComponentRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        componentDao = db.componentDao();
        allComponents = componentDao.getAllComponents();
    }

    public LiveData<List<Component>> getAllComponents() {
        return allComponents;
    }

    public LiveData<List<Component>> getComponentByCompositeItemId() {
        return componentsByCompositeItemId;
    }

    public synchronized void insert (Component component) {
        new insertAsyncTask(componentDao).execute(component);
    }

    public void delete(Component component) {
        new DeleteComponentAsyncTask(componentDao).execute(component);
    }

    public void update(Component component) {
        new UpdateComponentAsyncTask(componentDao).execute(component);
    }

    public void deleteAll(){
        new DeleteAllAsyncTask(componentDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Component, Void, Void> {
        private ComponentDao componentDao;

        DeleteAllAsyncTask(ComponentDao dao) {
            componentDao = dao;
        }

        @Override
        protected Void doInBackground(final Component... components) {
            componentDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Component, Void, Void> {
        private ComponentDao componentDao;

        insertAsyncTask(ComponentDao dao) {
            componentDao = dao;
        }

        @Override
        protected Void doInBackground(final Component... components) {
            componentDao.insert(components[0]);
            return null;
        }
    }

    private static class UpdateComponentAsyncTask extends AsyncTask<Component, Void, Void> {
        private ComponentDao componentDao;

        private UpdateComponentAsyncTask(ComponentDao componentDao) {
            this.componentDao = componentDao;
        }

        @Override
        protected Void doInBackground(Component... components) {
            componentDao.update(components[0]);
            return null;
        }
    }

    private static class DeleteComponentAsyncTask extends AsyncTask<Component, Void, Void> {
        private ComponentDao componentDao;

        private DeleteComponentAsyncTask(ComponentDao componentDao) {
            this.componentDao = componentDao;
        }

        @Override
        protected Void doInBackground(Component... components) {
            componentDao.delete(components[0]);
            return null;
        }
    }
}
