package com.ak17apps.bartenderassistant.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.database.UnitExchangeRepository;
import com.ak17apps.bartenderassistant.database.UnitRepository;
import com.ak17apps.bartenderassistant.entity.Unit;
import com.ak17apps.bartenderassistant.entity.UnitExchange;

import java.util.List;

public class UnitExchangeViewModel extends AndroidViewModel {
    private UnitExchangeRepository unitExchangeRepository;
    private LiveData<List<UnitExchange>> allUnitExchanges;

    public UnitExchangeViewModel(Application application) {
        super(application);
        unitExchangeRepository = new UnitExchangeRepository(application);
        allUnitExchanges = unitExchangeRepository.getAllUnitExchanges();
    }

    public LiveData<List<UnitExchange>> getAllUnitExchanges() { return allUnitExchanges; }
    public void insert(UnitExchange unitExchange) { unitExchangeRepository.insert(unitExchange); }
    public void update(UnitExchange unitExchange) { unitExchangeRepository.update(unitExchange); }
    public void delete(UnitExchange unitExchange) { unitExchangeRepository.delete(unitExchange); }
}