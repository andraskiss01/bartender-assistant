package com.ak17apps.bartenderassistant.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

import com.ak17apps.bartenderassistant.entity.Board;

@Dao
public interface BoardDao {
    @Insert
    void insert(Board board);

    @Update
    void update(Board board);

    @Delete
    void delete(Board board);

    @Query("DELETE FROM t_board")
    void deleteAll();

    @Query("SELECT * FROM t_board ORDER BY id")
    LiveData<List<Board>> getAllBoards();
}
