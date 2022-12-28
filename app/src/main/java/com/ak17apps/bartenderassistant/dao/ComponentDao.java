package com.ak17apps.bartenderassistant.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ak17apps.bartenderassistant.entity.Component;

import java.util.List;

@Dao
public interface ComponentDao {
    @Insert
    void insert(Component component);

    @Update
    void update(Component component);

    @Delete
    void delete(Component component);

    @Query("DELETE FROM t_component")
    void deleteAll();

    @Query("SELECT * FROM t_component WHERE composite_item_id = :compositeItemId")
    LiveData<List<Component>> getComponentByCompositeItemId(int compositeItemId);

    @Query("SELECT * FROM t_component ORDER BY id")
    LiveData<List<Component>> getAllComponents();

    @Query("SELECT * FROM t_component WHERE composite_item_id = :compositeItemId")
    List<Component> getComponentsByCompositeItemId(int compositeItemId);
}
