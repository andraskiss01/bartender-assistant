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
import com.ak17apps.bartenderassistant.adapter.ComponentListAdapter;
import com.ak17apps.bartenderassistant.adapter.RecyclerItemClickListener;
import com.ak17apps.bartenderassistant.entity.Component;
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;
import com.ak17apps.bartenderassistant.viewmodel.ComponentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ComponentActivity extends AppCompatActivity {
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_WORD_ACTIVITY_REQUEST_CODE = 2;

    private ComponentViewModel componentViewModel;
    private Button editButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component);
        setTitle(getResources().getString(R.string.components));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ComponentListAdapter adapter = new ComponentListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        componentViewModel = ViewModelProviders.of(this).get(ComponentViewModel.class);
        componentViewModel.getAllComponents().observe(this, new Observer<List<Component>>() {
            @Override
            public void onChanged(@Nullable final List<Component> components) {
                adapter.setComponents(components);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComponentActivity.this, NewComponentActivity.class);
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
                                componentViewModel.delete(adapter.getComponentAt(position));
                            }
                        });

                        editButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ComponentActivity.this, NewComponentActivity.class);
                                intent.putExtra(ApplicationStrings.COMPONENT_ID, adapter.getComponentAt(position).getId());
                                intent.putExtra(ApplicationStrings.COMPONENT_ITEM_ID, adapter.getComponentAt(position).getItemId());
                                intent.putExtra(ApplicationStrings.COMPONENT_ITEM, adapter.getComponentAt(position).getItem());
                                intent.putExtra(ApplicationStrings.COMPONENT_AMOUNT, Float.toString(adapter.getComponentAt(position).getAmount()));
                                intent.putExtra(ApplicationStrings.COMPONENT_UNIT_ID, adapter.getComponentAt(position).getUnitId());
                                intent.putExtra(ApplicationStrings.COMPONENT_UNIT, adapter.getComponentAt(position).getUnit());
                                intent.putExtra(ApplicationStrings.COMPONENT_COMPOSITE_ITEM_ID, adapter.getComponentAt(position).getCompositeItemId());
                                intent.putExtra(ApplicationStrings.COMPONENT_COMPOSITE_ITEM, adapter.getComponentAt(position).getCompositeItem());
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
            int itemId = intent.getIntExtra(ApplicationStrings.COMPONENT_ITEM_ID, -1);
            String item = intent.getStringExtra(ApplicationStrings.COMPONENT_ITEM);
            float amount = intent.getFloatExtra(ApplicationStrings.COMPONENT_AMOUNT, 0F);
            int unitId = intent.getIntExtra(ApplicationStrings.COMPONENT_UNIT_ID, -1);
            String unit = intent.getStringExtra(ApplicationStrings.COMPONENT_UNIT);
            int compositeItemId = intent.getIntExtra(ApplicationStrings.COMPONENT_COMPOSITE_ITEM_ID, -1);
            String compositeItem = intent.getStringExtra(ApplicationStrings.COMPONENT_COMPOSITE_ITEM);

            Component component = new Component(itemId, item, amount, unitId, unit, compositeItemId, compositeItem);
            componentViewModel.insert(component);
        } else if (requestCode == EDIT_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            int id = intent.getIntExtra(ApplicationStrings.COMPONENT_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            int itemId = intent.getIntExtra(ApplicationStrings.COMPONENT_ITEM_ID, -1);
            String item = intent.getStringExtra(ApplicationStrings.COMPONENT_ITEM);
            float amount = intent.getFloatExtra(ApplicationStrings.COMPONENT_AMOUNT, 0F);
            int unitId = intent.getIntExtra(ApplicationStrings.COMPONENT_UNIT_ID, -1);
            String unit = intent.getStringExtra(ApplicationStrings.COMPONENT_UNIT);
            int compositeItemId = intent.getIntExtra(ApplicationStrings.COMPONENT_COMPOSITE_ITEM_ID, -1);
            String compositeItem = intent.getStringExtra(ApplicationStrings.COMPONENT_COMPOSITE_ITEM);

            Component component = new Component(itemId, item, amount, unitId, unit, compositeItemId, compositeItem);
            component.setId(id);
            componentViewModel.update(component);
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
