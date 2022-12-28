package com.ak17apps.bartenderassistant.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.database.SupplyRepository;
import com.ak17apps.bartenderassistant.entity.Supply;

import java.util.List;

public class SupplyViewModel extends AndroidViewModel {
    private SupplyRepository supplyRepository;
    private LiveData<List<Supply>> allSupplies;

    public SupplyViewModel(Application application) {
        super(application);
        supplyRepository = new SupplyRepository(application);
        allSupplies = supplyRepository.getAllSupplies();
    }

    public LiveData<List<Supply>> getAllSupplies() { return allSupplies; }
    public void insert(Supply supply) { supplyRepository.insert(supply); }
    public void update(Supply supply) { supplyRepository.update(supply); }
    public void delete(Supply supply) { supplyRepository.delete(supply); }
}