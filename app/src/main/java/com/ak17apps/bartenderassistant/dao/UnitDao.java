package com.ak17apps.bartenderassistant.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;
import java.util.List;

import com.ak17apps.bartenderassistant.entity.Unit;

@Dao
public interface UnitDao{
	@Insert
    void insert(Unit unit);
	
	@Update
    void update(Unit unit);

    @Delete
    void delete(Unit unit);

    @Query("DELETE FROM t_unit")
    void deleteAll();

	@Query("SELECT * FROM t_unit ORDER BY id")
    LiveData<List<Unit>> getAllUnits();

	@Query("SELECT * FROM t_unit WHERE id = :id")
    Unit getUnitById(int id);
}