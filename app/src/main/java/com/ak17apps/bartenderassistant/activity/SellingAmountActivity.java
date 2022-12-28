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
import com.ak17apps.bartenderassistant.adapter.RecyclerItemClickListener;
import com.ak17apps.bartenderassistant.adapter.SellingAmountListAdapter;
import com.ak17apps.bartenderassistant.entity.SellingAmount;
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;
import com.ak17apps.bartenderassistant.viewmodel.SellingAmountViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SellingAmountActivity extends AppCompatActivity {
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_WORD_ACTIVITY_REQUEST_CODE = 2;

    private SellingAmountViewModel sellingAmountViewModel;
    private Button editButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_amount);
        setTitle(getResources().getString(R.string.selling_amounts));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final SellingAmountListAdapter adapter = new SellingAmountListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sellingAmountViewModel = ViewModelProviders.of(this).get(SellingAmountViewModel.class);
        sellingAmountViewModel.getAllSellingAmounts().observe(this, new Observer<List<SellingAmount>>() {
            @Override
            public void onChanged(@Nullable final List<SellingAmount> sellingAmounts) {
                adapter.setSellingAmounts(sellingAmounts);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellingAmountActivity.this, NewSellingAmountActivity.class);
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
                                sellingAmountViewModel.delete(adapter.getSellingAmountAt(position));
                            }
                        });

                        editButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SellingAmountActivity.this, NewSellingAmountActivity.class);
                                intent.putExtra(ApplicationStrings.SELLING_AMOUNT_ID, adapter.getSellingAmountAt(position).getId());
                                intent.putExtra(ApplicationStrings.SELLING_AMOUNT_ITEM_ID,  adapter.getSellingAmountAt(position).getItemId());
                                intent.putExtra(ApplicationStrings.SELLING_AMOUNT_ITEM,  adapter.getSellingAmountAt(position).getItem());
                                intent.putExtra(ApplicationStrings.SELLING_AMOUNT_AMOUNT, Float.toString(adapter.getSellingAmountAt(position).getAmount()));
                                intent.putExtra(ApplicationStrings.SELLING_AMOUNT_UNIT_ID,  adapter.getSellingAmountAt(position).getUnitId());
                                intent.putExtra(ApplicationStrings.SELLING_AMOUNT_UNIT,  adapter.getSellingAmountAt(position).getUnit());
                                intent.putExtra(ApplicationStrings.SELLING_AMOUNT_PRICE,  Float.toString(adapter.getSellingAmountAt(position).getPrice()));
                                intent.putExtra(ApplicationStrings.SELLING_AMOUNT_CATEGORY_ID,  adapter.getSellingAmountAt(position).getCategoryId());
                                intent.putExtra(ApplicationStrings.SELLING_AMOUNT_CATEGORY,  adapter.getSellingAmountAt(position).getCategory());
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
            int itemId = intent.getIntExtra(ApplicationStrings.SELLING_AMOUNT_ITEM_ID, -1);
            String item = intent.getStringExtra(ApplicationStrings.SELLING_AMOUNT_ITEM);
            float amount = intent.getFloatExtra(ApplicationStrings.SELLING_AMOUNT_AMOUNT, 0F);
            int unitId = intent.getIntExtra(ApplicationStrings.SELLING_AMOUNT_UNIT_ID, -1);
            String unit = intent.getStringExtra(ApplicationStrings.SELLING_AMOUNT_UNIT);
            float price = intent.getFloatExtra(ApplicationStrings.SELLING_AMOUNT_PRICE, 0F);
            int categoryId = intent.getIntExtra(ApplicationStrings.SELLING_AMOUNT_CATEGORY_ID, -1);
            String category = intent.getStringExtra(ApplicationStrings.SELLING_AMOUNT_CATEGORY);

            SellingAmount sellingAmount = new SellingAmount(amount, unitId, unit, price, itemId, item, categoryId, category);
            sellingAmountViewModel.insert(sellingAmount);
        } else if (requestCode == EDIT_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            int id = intent.getIntExtra(ApplicationStrings.SELLING_AMOUNT_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            int itemId = intent.getIntExtra(ApplicationStrings.SELLING_AMOUNT_ITEM_ID, -1);
            String item = intent.getStringExtra(ApplicationStrings.SELLING_AMOUNT_ITEM);
            float amount = intent.getFloatExtra(ApplicationStrings.SELLING_AMOUNT_AMOUNT, 0F);
            int unitId = intent.getIntExtra(ApplicationStrings.SELLING_AMOUNT_UNIT_ID, -1);
            String unit = intent.getStringExtra(ApplicationStrings.SELLING_AMOUNT_UNIT);
            float price = intent.getFloatExtra(ApplicationStrings.SELLING_AMOUNT_PRICE, 0F);
            int categoryId = intent.getIntExtra(ApplicationStrings.SELLING_AMOUNT_CATEGORY_ID, -1);
            String category = intent.getStringExtra(ApplicationStrings.SELLING_AMOUNT_CATEGORY);

            SellingAmount sellingAmount = new SellingAmount(amount, unitId, unit, price, itemId, item, categoryId, category);
            sellingAmount.setId(id);
            sellingAmountViewModel.update(sellingAmount);
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
