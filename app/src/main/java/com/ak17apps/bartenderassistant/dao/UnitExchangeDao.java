package com.ak17apps.bartenderassistant.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ak17apps.bartenderassistant.entity.UnitExchange;

import java.util.List;

@Dao
public interface UnitExchangeDao {
	@Insert
    void insert(UnitExchange unit);
	
	@Update
    void update(UnitExchange unit);

    @Delete
    void delete(UnitExchange unit);

    @Query("DELETE FROM t_unit_exchange")
    void deleteAll();

	@Query("SELECT * FROM t_unit_exchange ORDER BY id")
    LiveData<List<UnitExchange>> getAllUnitExchanges();

	@Query("SELECT * FROM t_unit_exchange WHERE id = :id")
    UnitExchange getUnitExchangeById(int id);

	@Query("SELECT * FROM t_unit_exchange WHERE from_unit_id = :fromUnitId AND to_unit_id = :toUnitId")
    List<UnitExchange> getUnitExchangeByFromUnitIdAndToUnitId(int fromUnitId, int toUnitId);
}