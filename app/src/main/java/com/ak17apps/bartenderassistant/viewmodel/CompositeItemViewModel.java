package com.ak17apps.bartenderassistant.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.database.CompositeItemRepository;
import com.ak17apps.bartenderassistant.entity.CompositeItem;

import java.util.List;

public class CompositeItemViewModel extends AndroidViewModel {
    private CompositeItemRepository compositeItemRepository;
    private LiveData<List<CompositeItem>> allCompositeItems;

    public CompositeItemViewModel(Application application) {
        super(application);
        compositeItemRepository = new CompositeItemRepository(application);
        allCompositeItems = compositeItemRepository.getAllCompositeItems();
    }

    public LiveData<Integer> getNumberOfCompositeItemsOfCategory(){
        return compositeItemRepository.getNumberOfCompositeItemsOfCategory();
    }
    public void setCategoryId(int categoryId){
        compositeItemRepository.setCategoryId(categoryId);
    }
    public LiveData<List<CompositeItem>> getAllCompositeItems() { return allCompositeItems; }
    public LiveData<List<CompositeItem>> getCompositeItemsOfCategory() { return compositeItemRepository.getCompositeItemsOfCategory(); }
    public void insert(CompositeItem compositeItem) { compositeItemRepository.insert(compositeItem); }
    public void update(CompositeItem compositeItem) { compositeItemRepository.update(compositeItem); }
    public void delete(CompositeItem compositeItem) { compositeItemRepository.delete(compositeItem); }
}