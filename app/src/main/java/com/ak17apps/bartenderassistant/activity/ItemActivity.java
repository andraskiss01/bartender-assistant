package com.ak17apps.bartenderassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ak17apps.bartenderassistant.R;
import com.ak17apps.bartenderassistant.adapter.ItemListAdapter;
import com.ak17apps.bartenderassistant.adapter.RecyclerItemClickListener;
import com.ak17apps.bartenderassistant.entity.Item;
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;
import com.ak17apps.bartenderassistant.viewmodel.ItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ItemActivity extends AppCompatActivity {
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_WORD_ACTIVITY_REQUEST_CODE = 2;

    private AppCompatActivity activity = this;
    private CoordinatorLayout layout;
    private ItemListAdapter adapter;
    private ItemViewModel itemViewModel;
    private Button editButton, deleteButton;
    private String lastSearchedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        setTitle(getResources().getString(R.string.products));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new ItemListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        layout = findViewById(R.id.item_layout);

        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        itemViewModel.getAllItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable final List<Item> items) {
                adapter.setItems(items);
            }
        });

        FloatingActionButton searchButton = findViewById(R.id.magnifier);
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                search();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemActivity.this, NewItemActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, final int position) {
                        editButton = v.findViewById(R.id.button_edit);
                        deleteButton = v.findViewById(R.id.button_delete);

                        deleteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                itemViewModel.delete(adapter.getItemAt(position));
                            }
                        });

                        editButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ItemActivity.this, NewItemActivity.class);
                                intent.putExtra(ApplicationStrings.ITEM_ID, adapter.getItemAt(position).getId());
                                intent.putExtra(ApplicationStrings.ITEM_NAME, adapter.getItemAt(position).getName());
                                intent.putExtra(ApplicationStrings.ITEM_STORED_AMOUNT,  Float.toString(adapter.getItemAt(position).getStoredAmount()));
                                intent.putExtra(ApplicationStrings.ITEM_PACKAGING_UNIT_ID,  adapter.getItemAt(position).getPackagingUnitId());
                                intent.putExtra(ApplicationStrings.ITEM_PACKAGING_UNIT,  adapter.getItemAt(position).getPackagingUnit());
                                intent.putExtra(ApplicationStrings.ITEM_SELLABLE_INDIVIDUALLY,  adapter.getItemAt(position).isSellableIndividually());
                                intent.putExtra(ApplicationStrings.ITEM_MAX_AMOUNT,  Float.toString(adapter.getItemAt(position).getMaxAmount()));
                                startActivityForResult(intent, EDIT_WORD_ACTIVITY_REQUEST_CODE);
                            }
                        });
                    }
                })
        );
    }

    public void search() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popupView = inflater.inflate(R.layout.search_window, null);

        int width = (int) (layout.getWidth() * 0.8);
        int height = (int) (layout.getHeight() * 0.17);

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        final EditText textET = popupView.findViewById(R.id.textET);
        textET.setText(lastSearchedItem != null ? lastSearchedItem : "");

        Button searchBtn = popupView.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSearchedItem = textET.getText().toString();
                itemViewModel.setItemName(lastSearchedItem);
                itemViewModel.getItemsByName().observe(activity, new Observer<List<Item>>() {
                    @Override
                    public void onChanged(@Nullable final List<Item> items) {
                        adapter.setItems(items);
                    }
                });
                popupWindow.dismiss();
            }
        });

        Button clearBtn = popupView.findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textET.setText(null);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String name = intent.getStringExtra(ApplicationStrings.ITEM_NAME);
            float storedAmount = intent.getFloatExtra(ApplicationStrings.ITEM_STORED_AMOUNT, 0F);
            int packagingUnitId = intent.getIntExtra(ApplicationStrings.ITEM_PACKAGING_UNIT_ID, -1);
            String packagingUnit = intent.getStringExtra(ApplicationStrings.ITEM_PACKAGING_UNIT);
            boolean sellableIndividually = intent.getBooleanExtra(ApplicationStrings.ITEM_SELLABLE_INDIVIDUALLY, true);
            float maxAmount = intent.getFloatExtra(ApplicationStrings.ITEM_MAX_AMOUNT, 0F);

            Item item = new Item(name, storedAmount, packagingUnitId, packagingUnit, sellableIndividually, maxAmount);
            itemViewModel.insert(item);
        } else if (requestCode == EDIT_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            int id = intent.getIntExtra(ApplicationStrings.ITEM_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = intent.getStringExtra(ApplicationStrings.ITEM_NAME);
            float storedAmount = intent.getFloatExtra(ApplicationStrings.ITEM_STORED_AMOUNT, 0);
            int packagingUnitId = intent.getIntExtra(ApplicationStrings.ITEM_PACKAGING_UNIT_ID, -1);
            String packagingUnit = intent.getStringExtra(ApplicationStrings.ITEM_PACKAGING_UNIT);
            boolean sellableIndividually = intent.getBooleanExtra(ApplicationStrings.ITEM_SELLABLE_INDIVIDUALLY, true);
            float maxAmount = intent.getFloatExtra(ApplicationStrings.ITEM_MAX_AMOUNT, 0);

            Item item = new Item(name, storedAmount, packagingUnitId, packagingUnit, sellableIndividually, maxAmount);
            item.setId(id);
            itemViewModel.update(item);
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MaintenanceActivity.class));
        finish();
        overridePendingTransition(0, 0);
    }
}
