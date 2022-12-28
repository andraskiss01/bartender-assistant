package com.ak17apps.bartenderassistant.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ak17apps.bartenderassistant.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    @Query("DELETE FROM t_category")
    void deleteAll();

    @Query("SELECT * FROM t_category WHERE parent_category_id = :categoryId ORDER BY name")
    LiveData<List<Category>> getCategoriesOfParentCategoryId(int categoryId);

    @Query("SELECT * FROM t_category ORDER BY id")
    LiveData<List<Category>> getAllCategories();
}
