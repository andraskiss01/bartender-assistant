package com.ak17apps.bartenderassistant.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;
import java.util.List;

import com.ak17apps.bartenderassistant.entity.SellingAmount;

@Dao
public interface SellingAmountDao{
	@Insert
    void insert(SellingAmount sellingAmount);
	
	@Update
    void update(SellingAmount sellingAmount);

    @Delete
    void delete(SellingAmount sellingAmount);

    @Query("DELETE FROM t_selling_amount")
    void deleteAll();
	
	@Query("SELECT * FROM t_selling_amount WHERE category_id = :categoryId")
    LiveData<List<SellingAmount>> getSellingAmountsOfCategoryId(int categoryId);

	@Query("SELECT * FROM t_selling_amount ORDER BY id")
    LiveData<List<SellingAmount>> getAllSellingAmounts();

	@Query("SELECT * FROM t_selling_amount WHERE id = :id")
    SellingAmount getSellingAmountById(int id);
}