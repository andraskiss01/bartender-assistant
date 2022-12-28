package com.ak17apps.bartenderassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ak17apps.bartenderassistant.R;
import com.ak17apps.bartenderassistant.entity.Board;
import com.ak17apps.bartenderassistant.viewmodel.BoardViewModel;

import java.util.List;

public class BoardListAdapter extends RecyclerView.Adapter<BoardListAdapter.BoardViewHolder> {
    private BoardViewModel boardViewModel;
    private final LayoutInflater inflater;
    private List<Board> boards; // Cached copy of words

    public BoardListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    class BoardViewHolder extends RecyclerView.ViewHolder {
        private final TextView boardNameView;
        private final Button addButton, editButton, deleteButton;
        private BoardViewHolder(final View boardView) {
            super(boardView);
            boardNameView = boardView.findViewById(R.id.textView_board_name);

            addButton = boardView.findViewById(R.id.button_open);
            editButton = boardView.findViewById(R.id.button_edit);
            deleteButton = boardView.findViewById(R.id.button_delete);
        }
    }

    @Override
    public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View boardView = inflater.inflate(R.layout.recyclerview_board, parent, false);
        return new BoardViewHolder(boardView);
    }

    @Override
    public void onBindViewHolder(final BoardViewHolder holder, int position) {
        if (boards != null) {
            Board current = boards.get(position);
            holder.boardNameView.setText(current.getName());
        } else {
            holder.boardNameView.setText("No Item");
        }
    }

    public void setBoards(List<Board> boards){
        this.boards = boards;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (boards != null) {
            return boards.size();
        }else {
            return 0;
        }
    }

    public Board getBoardAt(int position) {
        return boards.get(position);
    }
}
