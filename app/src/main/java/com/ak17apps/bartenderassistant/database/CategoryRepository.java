package com.ak17apps.bartenderassistant.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.dao.CategoryDao;
import com.ak17apps.bartenderassistant.entity.Category;

import java.util.List;

public class CategoryRepository {
    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategories;
    private LiveData<List<Category>> categoriesOfParentCategory;

    public CategoryRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        categoryDao = db.categoryDao();
        allCategories = categoryDao.getAllCategories();
    }

    public void setParentCategoryId(int parentCategoryId){
        categoriesOfParentCategory = categoryDao.getCategoriesOfParentCategoryId(parentCategoryId);
    }

    public LiveData<List<Category>> getCategoriesOfParentCategory() {
        return categoriesOfParentCategory;
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public synchronized void insert (Category category) {
        new insertAsyncTask(categoryDao).execute(category);
    }

    public void delete(Category category) {
        new DeleteCategoryAsyncTask(categoryDao).execute(category);
    }

    public void update(Category category) {
        new UpdateCategoryAsyncTask(categoryDao).execute(category);
    }

    public void deleteAll(){
        new DeleteAllAsyncTask(categoryDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao categoryDao;

        DeleteAllAsyncTask(CategoryDao dao) {
            categoryDao = dao;
        }

        @Override
        protected Void doInBackground(final Category... categories) {
            categoryDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao categoryDao;

        insertAsyncTask(CategoryDao dao) {
            categoryDao = dao;
        }

        @Override
        protected Void doInBackground(final Category... categories) {
            categoryDao.insert(categories[0]);
            return null;
        }
    }

    private static class UpdateCategoryAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao categoryDao;

        private UpdateCategoryAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.update(categories[0]);
            return null;
        }
    }

    private static class DeleteCategoryAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao categoryDao;

        private DeleteCategoryAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.delete(categories[0]);
            return null;
        }
    }
}
