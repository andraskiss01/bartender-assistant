package com.ak17apps.bartenderassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ak17apps.bartenderassistant.R;
import com.ak17apps.bartenderassistant.adapter.OrderListAdapter;
import com.ak17apps.bartenderassistant.adapter.RecyclerItemClickListener;
import com.ak17apps.bartenderassistant.entity.Order;
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;
import com.ak17apps.bartenderassistant.viewmodel.OrderViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class OrderActivity extends AppCompatActivity {
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    private OrderViewModel orderViewModel;
    private Button deleteButton;
    private Button signButton;
    private Button tickButton;
    private int boardId;
    private String boardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent intent = getIntent();
        boardId = intent.getIntExtra(ApplicationStrings.BOARD_ID, -1);
        boardName = intent.getStringExtra(ApplicationStrings.BOARD_NAME);

        setTitle(boardName + " " + getResources().getString(R.string.orders_of));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final OrderListAdapter adapter = new OrderListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        orderViewModel.setBoardId(boardId);
        orderViewModel.getOrdersOfBoard().observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(@Nullable final List<Order> orders) {
                adapter.setOrders(orders);
            }
        });

        final TextView sumTV = findViewById(R.id.sumTV);
        orderViewModel.getSumPrice().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float sum) {
                sumTV.setText(getResources().getString(R.string.sum) + ": " + sum + " Ft");
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, NewOrderWithCategoriesActivity.class);
                intent.putExtra(ApplicationStrings.BOARD_ID, boardId);
                intent.putExtra(ApplicationStrings.BOARD_NAME, boardName);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, final int position) {
                        deleteButton = v.findViewById(R.id.button_delete);
                        deleteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                orderViewModel.delete(adapter.getOrderAt(position));
                            }
                        });

                        signButton = v.findViewById(R.id.button_sign);
                        signButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                orderViewModel.sign(adapter.getOrderAt(position));
                            }
                        });

                        tickButton = v.findViewById(R.id.button_tick);
                        tickButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                orderViewModel.tick(adapter.getOrderAt(position));
                            }
                        });
                    }
                })
        );
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Order order = new Order();
            order.setBoard(data.getStringExtra(ApplicationStrings.BOARD_NAME));

            orderViewModel.insert(order);
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, BoardActivity.class));
        finish();
        overridePendingTransition(0, 0);
    }
}
