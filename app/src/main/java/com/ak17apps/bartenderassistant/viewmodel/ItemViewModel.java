package com.ak17apps.bartenderassistant.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.database.ItemRepository;
import com.ak17apps.bartenderassistant.entity.Item;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private ItemRepository itemRepository;
    private String itemName;
    private LiveData<List<Item>> allItems;

    public ItemViewModel (Application application) {
        super(application);
        itemRepository = new ItemRepository(application);
        allItems = itemRepository.getAllItems();
    }

    public void setItemName(String itemName){
        itemRepository.setItemName(itemName);
    }
    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }
    public LiveData<List<Item>> getItemsByName() {
        return itemRepository.getItemsByName();
    }
    public void insert(Item item) { itemRepository.insert(item); }
    public void update(Item item) { itemRepository.update(item); }
    public void delete(Item item) { itemRepository.delete(item); }
}