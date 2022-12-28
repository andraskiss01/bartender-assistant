package com.ak17apps.bartenderassistant.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.database.CategoryRepository;
import com.ak17apps.bartenderassistant.entity.Category;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository categoryRepository;
    private LiveData<List<Category>> allCategories;

    public CategoryViewModel(Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        allCategories = categoryRepository.getAllCategories();
    }

    public void setParentCategoryId(int parentCategoryId){ categoryRepository.setParentCategoryId(parentCategoryId); }
    public LiveData<List<Category>> getCategoriesOfParentCategory() { return categoryRepository.getCategoriesOfParentCategory(); }
    public LiveData<List<Category>> getAllCategories() { return allCategories; }
    public void insert(Category category) { categoryRepository.insert(category); }
    public void update(Category category) { categoryRepository.update(category); }
    public void delete(Category category) { categoryRepository.delete(category); }
}