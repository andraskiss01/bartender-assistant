package com.ak17apps.bartenderassistant.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import com.ak17apps.bartenderassistant.entity.Order;

import java.util.List;

@Dao
public interface OrderDao{
	@Insert
    void insert(Order order);
	
	@Update
    void update(Order order);

    @Delete
    void delete(Order order);

    @Query("DELETE FROM t_order")
    void deleteAll();
	
	@Query("SELECT * FROM t_order WHERE board_id = :boardId ORDER BY orderable")
    LiveData<List<Order>> getOrdersOfBoard(int boardId);

	@Query("SELECT * FROM t_order WHERE board_id = :boardId ORDER BY orderable")
    List<Order> getOrderListOfBoard(int boardId);

	@Query("SELECT * FROM t_order ORDER BY id")
    LiveData<List<Order>> getAllOrders();

	@Query("SELECT sum(price) FROM t_order WHERE board_id = :boardId")
    LiveData<Float> getSumPriceOfBoard(int boardId);
}