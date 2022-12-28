package com.ak17apps.bartenderassistant.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.database.BoardRepository;
import com.ak17apps.bartenderassistant.database.ItemRepository;
import com.ak17apps.bartenderassistant.entity.Board;
import com.ak17apps.bartenderassistant.entity.Item;

import java.util.List;

public class BoardViewModel extends AndroidViewModel {
    private BoardRepository boardRepository;
    private LiveData<List<Board>> allBoards;

    public BoardViewModel(Application application) {
        super(application);
        boardRepository = new BoardRepository(application);
        allBoards = boardRepository.getAllBoards();
    }

    public LiveData<List<Board>> getAllBoards() { return allBoards; }
    public void insert(Board board) { boardRepository.insert(board); }
    public void update(Board board) { boardRepository.update(board); }
    public void delete(Board board) { boardRepository.delete(board); }
    public void tick(Board board) { boardRepository.tick(board); }
}