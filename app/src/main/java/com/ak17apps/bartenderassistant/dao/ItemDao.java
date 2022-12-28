package com.ak17apps.bartenderassistant.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ak17apps.bartenderassistant.entity.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert(Item item);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);

    @Query("DELETE FROM t_item")
    void deleteAll();

    @Query("SELECT * FROM t_item ORDER BY id")
    LiveData<List<Item>> getAllItem();

    @Query("SELECT * FROM t_item WHERE name LIKE '%' || :itemName || '%' ORDER BY id")
    LiveData<List<Item>> getItemsByName(String itemName);

    @Query("SELECT * FROM t_item WHERE id = :id")
    Item getItemById(int id);
}
