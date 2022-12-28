package com.ak17apps.bartenderassistant.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.database.ComponentRepository;
import com.ak17apps.bartenderassistant.entity.Component;

import java.util.List;

public class ComponentViewModel extends AndroidViewModel {
    private ComponentRepository componentRepository;
    private LiveData<List<Component>> allComponentsById;
    private LiveData<List<Component>> allComponents;

    public ComponentViewModel(Application application, int componentId) {
        super(application);
        componentRepository = new ComponentRepository(application, componentId);
        allComponentsById = componentRepository.getComponentByCompositeItemId();
    }

    public ComponentViewModel(Application application) {
        super(application);
        componentRepository = new ComponentRepository(application);
        allComponents = componentRepository.getAllComponents();
    }

    public LiveData<List<Component>> getAllComponents() { return allComponents; }
    public LiveData<List<Component>> getAllComponentsById() { return allComponentsById; }
    public void insert(Component component) { componentRepository.insert(component); }
    public void update(Component component) { componentRepository.update(component); }
    public void delete(Component component) { componentRepository.delete(component); }
}