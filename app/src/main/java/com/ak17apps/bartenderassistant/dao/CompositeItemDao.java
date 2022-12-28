package com.ak17apps.bartenderassistant.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;
import java.util.List;

import com.ak17apps.bartenderassistant.entity.CompositeItem;

@Dao
public interface CompositeItemDao{
	@Insert
    void insert(CompositeItem compositeItem);
	
	@Update
    void update(CompositeItem compositeItem);

    @Delete
    void delete(CompositeItem compositeItem);

    @Query("DELETE FROM t_composite_item")
    void deleteAll();
	
	@Query("SELECT * FROM t_composite_item WHERE category_id = :categoryId ORDER BY name")
    LiveData<List<CompositeItem>> getCompositeItemsOfCategory(int categoryId);

	@Query("SELECT COUNT(*) FROM t_composite_item WHERE category_id = :categoryId")
    LiveData<Integer> getNumberOfCompositeItemsOfCategory(int categoryId);

	@Query("SELECT * FROM t_composite_item ORDER BY id")
    LiveData<List<CompositeItem>> getAllCompositeItems();

	@Query("SELECT * FROM t_composite_item WHERE id = :id")
    CompositeItem getCompositeItemById(int id);
}