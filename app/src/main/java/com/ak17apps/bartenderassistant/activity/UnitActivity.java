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
import com.ak17apps.bartenderassistant.adapter.UnitListAdapter;
import com.ak17apps.bartenderassistant.entity.Unit;
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;
import com.ak17apps.bartenderassistant.viewmodel.UnitViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class UnitActivity extends AppCompatActivity {
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_WORD_ACTIVITY_REQUEST_CODE = 2;

    private UnitViewModel unitViewModel;
    private Button editButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        setTitle(getResources().getString(R.string.units));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final UnitListAdapter adapter = new UnitListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        unitViewModel = ViewModelProviders.of(this).get(UnitViewModel.class);

        unitViewModel.getAllUnits().observe(this, new Observer<List<Unit>>() {
            @Override
            public void onChanged(@Nullable final List<Unit> units) {
                adapter.setUnits(units);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UnitActivity.this, NewUnitActivity.class);
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
                                unitViewModel.delete(adapter.getUnitAt(position));
                            }
                        });

                        editButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(UnitActivity.this, NewUnitActivity.class);
                                intent.putExtra(ApplicationStrings.UNIT_ID, adapter.getUnitAt(position).getId());
                                intent.putExtra(ApplicationStrings.UNIT_NAME, adapter.getUnitAt(position).getName());
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
            String name = data.getStringExtra(ApplicationStrings.UNIT_NAME);

            Unit unit = new Unit(name);
            unitViewModel.insert(unit);
        } else if (requestCode == EDIT_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            int id = data.getIntExtra(ApplicationStrings.UNIT_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = data.getStringExtra(ApplicationStrings.UNIT_NAME);

            Unit unit = new Unit(name);
            unit.setId(id);
            unitViewModel.update(unit);
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
