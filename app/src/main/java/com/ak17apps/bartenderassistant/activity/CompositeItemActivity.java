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
import com.ak17apps.bartenderassistant.adapter.CompositeItemListAdapter;
import com.ak17apps.bartenderassistant.adapter.RecyclerItemClickListener;
import com.ak17apps.bartenderassistant.entity.CompositeItem;
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;
import com.ak17apps.bartenderassistant.viewmodel.CompositeItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CompositeItemActivity extends AppCompatActivity {
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_WORD_ACTIVITY_REQUEST_CODE = 2;

    private CompositeItemViewModel compositeItemViewModel;
    private Button editButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        setTitle(getResources().getString(R.string.composite_items));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final CompositeItemListAdapter adapter = new CompositeItemListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        compositeItemViewModel = ViewModelProviders.of(this).get(CompositeItemViewModel.class);
        compositeItemViewModel.getAllCompositeItems().observe(this, new Observer<List<CompositeItem>>() {
            @Override
            public void onChanged(@Nullable final List<CompositeItem> compositeItems) {
                adapter.setCompositeItems(compositeItems);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompositeItemActivity.this, NewCompositeItemActivity.class);
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
                                compositeItemViewModel.delete(adapter.getCompositeItemAt(position));
                            }
                        });

                        editButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CompositeItemActivity.this, NewCompositeItemActivity.class);
                                intent.putExtra(ApplicationStrings.COMPOSITE_ITEM_ID, adapter.getCompositeItemAt(position).getId());
                                intent.putExtra(ApplicationStrings.COMPOSITE_ITEM_NAME, adapter.getCompositeItemAt(position).getName());
                                intent.putExtra(ApplicationStrings.COMPOSITE_ITEM_PRICE,  Float.toString(adapter.getCompositeItemAt(position).getPrice()));
                                intent.putExtra(ApplicationStrings.COMPOSITE_ITEM_CATEGORY_ID,  adapter.getCompositeItemAt(position).getCategoryId());
                                intent.putExtra(ApplicationStrings.COMPOSITE_ITEM_CATEGORY,  adapter.getCompositeItemAt(position).getCategory());
                                startActivityForResult(intent, EDIT_WORD_ACTIVITY_REQUEST_CODE);
                            }
                        });
                    }
                })
        );
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String name = intent.getStringExtra(ApplicationStrings.COMPOSITE_ITEM_NAME);
            float price = intent.getFloatExtra(ApplicationStrings.COMPOSITE_ITEM_PRICE, 0F);
            int categoryId = intent.getIntExtra(ApplicationStrings.COMPOSITE_ITEM_CATEGORY_ID, -1);
            String category = intent.getStringExtra(ApplicationStrings.COMPOSITE_ITEM_CATEGORY);

            CompositeItem compositeItem = new CompositeItem(name, price, categoryId, category);
            compositeItemViewModel.insert(compositeItem);
        } else if (requestCode == EDIT_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            int id = intent.getIntExtra(ApplicationStrings.COMPOSITE_ITEM_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = intent.getStringExtra(ApplicationStrings.COMPOSITE_ITEM_NAME);
            float price = intent.getFloatExtra(ApplicationStrings.COMPOSITE_ITEM_PRICE, 0F);
            int categoryId = intent.getIntExtra(ApplicationStrings.COMPOSITE_ITEM_CATEGORY_ID, -1);
            String category = intent.getStringExtra(ApplicationStrings.COMPOSITE_ITEM_CATEGORY);

            CompositeItem compositeItem = new CompositeItem(name, price, categoryId, category);
            compositeItem.setId(id);
            compositeItemViewModel.update(compositeItem);
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
