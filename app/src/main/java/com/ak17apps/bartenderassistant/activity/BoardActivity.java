package com.ak17apps.bartenderassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ak17apps.bartenderassistant.R;
import com.ak17apps.bartenderassistant.adapter.BoardListAdapter;
import com.ak17apps.bartenderassistant.adapter.RecyclerItemClickListener;
import com.ak17apps.bartenderassistant.entity.Board;
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;
import com.ak17apps.bartenderassistant.viewmodel.BoardViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class BoardActivity extends AppCompatActivity {
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_WORD_ACTIVITY_REQUEST_CODE = 2;

    private BoardViewModel boardViewModel;
    private Button addButton, editButton, deleteButton, tickButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        setTitle(getResources().getString(R.string.tables));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BoardListAdapter adapter = new BoardListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        boardViewModel = ViewModelProviders.of(this).get(BoardViewModel.class);

        boardViewModel.getAllBoards().observe(this, new Observer<List<Board>>() {
            @Override
            public void onChanged(@Nullable final List<Board> boards) {
                adapter.setBoards(boards);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BoardActivity.this, NewBoardActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, final int position) {
                        addButton = v.findViewById(R.id.button_open);
                        editButton = v.findViewById(R.id.button_edit);
                        deleteButton = v.findViewById(R.id.button_delete);
                        tickButton = v.findViewById(R.id.button_tick);

                        addButton.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(BoardActivity.this, OrderActivity.class);
                                intent.putExtra(ApplicationStrings.BOARD_ID, adapter.getBoardAt(position).getId());
                                intent.putExtra(ApplicationStrings.BOARD_NAME, adapter.getBoardAt(position).getName());
                                startActivity(intent);
                                finish();
                                overridePendingTransition(0, 0);
                            }
                        });

                        deleteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boardViewModel.delete(adapter.getBoardAt(position));
                            }
                        });

                        editButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(BoardActivity.this, NewBoardActivity.class);
                                intent.putExtra(ApplicationStrings.BOARD_ID, adapter.getBoardAt(position).getId());
                                intent.putExtra(ApplicationStrings.BOARD_NAME, adapter.getBoardAt(position).getName());
                                startActivityForResult(intent, EDIT_WORD_ACTIVITY_REQUEST_CODE);
                            }
                        });

                        tickButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boardViewModel.tick(adapter.getBoardAt(position));
                            }
                        });
                    }
                })
        );
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            boardViewModel.insert(new Board(data.getStringExtra(ApplicationStrings.BOARD_NAME)));
        } else if (requestCode == EDIT_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            int id = data.getIntExtra(ApplicationStrings.BOARD_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            Board board = new Board(data.getStringExtra(ApplicationStrings.BOARD_NAME));
            board.setId(id);
            boardViewModel.update(board);
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
        overridePendingTransition(0, 0);
    }
}
