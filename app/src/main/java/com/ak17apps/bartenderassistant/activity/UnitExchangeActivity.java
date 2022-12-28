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
import com.ak17apps.bartenderassistant.adapter.UnitExchangeListAdapter;
import com.ak17apps.bartenderassistant.entity.UnitExchange;
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;
import com.ak17apps.bartenderassistant.viewmodel.UnitExchangeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class UnitExchangeActivity extends AppCompatActivity {
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_WORD_ACTIVITY_REQUEST_CODE = 2;

    private UnitExchangeViewModel unitExchangeViewModel;
    private Button editButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_exchange);
        setTitle(getResources().getString(R.string.unit_exchanges));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final UnitExchangeListAdapter adapter = new UnitExchangeListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        unitExchangeViewModel = ViewModelProviders.of(this).get(UnitExchangeViewModel.class);

        unitExchangeViewModel.getAllUnitExchanges().observe(this, new Observer<List<UnitExchange>>() {
            @Override
            public void onChanged(@Nullable final List<UnitExchange> unitExchanges) {
                adapter.setUnitExchanges(unitExchanges);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UnitExchangeActivity.this, NewUnitExchangeActivity.class);
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
                                unitExchangeViewModel.delete(adapter.getUnitExchangeAt(position));
                            }
                        });

                        editButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(UnitExchangeActivity.this, NewUnitExchangeActivity.class);
                                intent.putExtra(ApplicationStrings.UNIT_EXCHANGE_ID, adapter.getUnitExchangeAt(position).getId());
                                intent.putExtra(ApplicationStrings.UNIT_EXCHANGE_FROM_UNIT_ID, adapter.getUnitExchangeAt(position).getFromUnitId());
                                intent.putExtra(ApplicationStrings.UNIT_EXCHANGE_FROM_UNIT, adapter.getUnitExchangeAt(position).getFromUnit());
                                intent.putExtra(ApplicationStrings.UNIT_EXCHANGE_FROM_VALUE, Float.toString(adapter.getUnitExchangeAt(position).getFromValue()));
                                intent.putExtra(ApplicationStrings.UNIT_EXCHANGE_TO_UNIT_ID, adapter.getUnitExchangeAt(position).getToUnitId());
                                intent.putExtra(ApplicationStrings.UNIT_EXCHANGE_TO_UNIT, adapter.getUnitExchangeAt(position).getToUnit());
                                intent.putExtra(ApplicationStrings.UNIT_EXCHANGE_TO_VALUE, Float.toString(adapter.getUnitExchangeAt(position).getToValue()));
                                startActivityForResult(intent, EDIT_WORD_ACTIVITY_REQUEST_CODE);
                            }
                        });
                    }
                })
        );
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            int fromUnitId = data.getIntExtra(ApplicationStrings.UNIT_EXCHANGE_FROM_UNIT_ID, -1);
            String fromUnit = data.getStringExtra(ApplicationStrings.UNIT_EXCHANGE_FROM_UNIT);
            float fromValue = data.getFloatExtra(ApplicationStrings.UNIT_EXCHANGE_FROM_VALUE, 0F);
            int toUnitId = data.getIntExtra(ApplicationStrings.UNIT_EXCHANGE_TO_UNIT_ID, -1);
            String toUnit = data.getStringExtra(ApplicationStrings.UNIT_EXCHANGE_TO_UNIT);
            float toValue = data.getFloatExtra(ApplicationStrings.UNIT_EXCHANGE_TO_VALUE, 0F);

            unitExchangeViewModel.insert(new UnitExchange(fromUnitId, fromUnit, fromValue, toUnitId, toUnit, toValue));
        } else if (requestCode == EDIT_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            int id = data.getIntExtra(ApplicationStrings.UNIT_EXCHANGE_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            int fromUnitId = data.getIntExtra(ApplicationStrings.UNIT_EXCHANGE_FROM_UNIT_ID, -1);
            String fromUnit = data.getStringExtra(ApplicationStrings.UNIT_EXCHANGE_FROM_UNIT);
            float fromValue = data.getFloatExtra(ApplicationStrings.UNIT_EXCHANGE_FROM_VALUE, 0F);
            int toUnitId = data.getIntExtra(ApplicationStrings.UNIT_EXCHANGE_TO_UNIT_ID, -1);
            String toUnit = data.getStringExtra(ApplicationStrings.UNIT_EXCHANGE_TO_UNIT);
            float toValue = data.getFloatExtra(ApplicationStrings.UNIT_EXCHANGE_TO_VALUE, 0F);

            UnitExchange unitExchange = new UnitExchange(fromUnitId, fromUnit, fromValue, toUnitId, toUnit, toValue);
            unitExchange.setId(id);
            unitExchangeViewModel.update(unitExchange);
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
