package com.ak17apps.bartenderassistant.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.database.UnitRepository;
import com.ak17apps.bartenderassistant.entity.Unit;

import java.util.List;

public class UnitViewModel extends AndroidViewModel {
    private UnitRepository unitRepository;
    private LiveData<List<Unit>> allUnits;

    public UnitViewModel(Application application) {
        super(application);
        unitRepository = new UnitRepository(application);
        allUnits = unitRepository.getAllUnits();
    }

    public LiveData<List<Unit>> getAllUnits() { return allUnits; }
    public String getUnitNameById(int id) { return unitRepository.getUnitNameById(id); }
    public void insert(Unit unit) { unitRepository.insert(unit); }
    public void update(Unit unit) { unitRepository.update(unit); }
    public void delete(Unit unit) { unitRepository.delete(unit); }
}