package com.ak17apps.bartenderassistant.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ak17apps.bartenderassistant.entity.Supply;
import com.ak17apps.bartenderassistant.entity.Unit;

import java.util.List;

@Dao
public interface SupplyDao {
	@Insert
    void insert(Supply supply);
	
	@Update
    void update(Supply supply);

    @Query("DELETE FROM t_supply")
    void deleteAll();

    @Delete
    void delete(Supply supply);
	
	@Query("SELECT * FROM t_supply ORDER BY id")
    LiveData<List<Supply>> getAllSupplies();
}