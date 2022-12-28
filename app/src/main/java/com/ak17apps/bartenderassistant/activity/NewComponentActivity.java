package com.ak17apps.bartenderassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ak17apps.bartenderassistant.R;
import com.ak17apps.bartenderassistant.entity.CompositeItem;
import com.ak17apps.bartenderassistant.entity.Item;
import com.ak17apps.bartenderassistant.entity.Unit;
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;
import com.ak17apps.bartenderassistant.viewmodel.CompositeItemViewModel;
import com.ak17apps.bartenderassistant.viewmodel.ItemViewModel;
import com.ak17apps.bartenderassistant.viewmodel.UnitViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewComponentActivity extends AppCompatActivity {
    private EditText amountView;
    private Spinner itemSpinner, unitSpinner, compositeItemSpinner;
    private ItemViewModel itemViewModel;
    private UnitViewModel unitViewModel;
    private CompositeItemViewModel compositeItemViewModel;
    private AppCompatActivity activity = this;
    private List<Item> itemList;
    private List<Unit> unitList;
    private List<CompositeItem> compositeItemList;
    private String selectedItem, selectedUnit, selectedCompositeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_component);

        itemSpinner = findViewById(R.id.edit_component_item);
        amountView = findViewById(R.id.edit_component_amount);
        unitSpinner = findViewById(R.id.edit_component_unit);
        compositeItemSpinner = findViewById(R.id.edit_component_composite_item);

        Intent intent = getIntent();
        if (intent.hasExtra(ApplicationStrings.COMPONENT_ID)) {
            setTitle(getResources().getString(R.string.edit_component));
            selectedItem = intent.getStringExtra(ApplicationStrings.COMPONENT_ITEM);
            amountView.setText(intent.getStringExtra(ApplicationStrings.COMPONENT_AMOUNT));
            selectedUnit = intent.getStringExtra(ApplicationStrings.COMPONENT_UNIT);
            selectedCompositeItem = intent.getStringExtra(ApplicationStrings.COMPONENT_COMPOSITE_ITEM);
        } else {
            setTitle(getResources().getString(R.string.new_component));
        }

        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        itemViewModel.getAllItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable final List<Item> items) {
                if(items == null){
                    return;
                }

                itemList = items;

                List<String> unitStrList = new ArrayList<>();
                for(Item item : items){
                    unitStrList.add(item.getName());
                }

                int selectedPosition = -1;
                selectedPosition = unitStrList.indexOf(selectedItem);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, unitStrList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                itemSpinner.setAdapter(dataAdapter);
                if(selectedPosition != -1) {
                    itemSpinner.setSelection(selectedPosition);
                }
            }
        });

        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemName = parent.getItemAtPosition(position).toString();
                itemSpinner.setPrompt(itemName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        unitViewModel = ViewModelProviders.of(this).get(UnitViewModel.class);
        unitViewModel.getAllUnits().observe(this, new Observer<List<Unit>>() {
            @Override
            public void onChanged(@Nullable final List<Unit> units) {
                if(units == null){
                    return;
                }

                unitList = units;

                List<String> unitStrList = new ArrayList<>();
                for(Unit unit : units){
                    unitStrList.add(unit.getName());
                }

                int selectedPosition = -1;
                selectedPosition = unitStrList.indexOf(selectedUnit);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, unitStrList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                unitSpinner.setAdapter(dataAdapter);
                if(selectedPosition != -1) {
                    unitSpinner.setSelection(selectedPosition);
                }
            }
        });

        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String unitName = parent.getItemAtPosition(position).toString();
                unitSpinner.setPrompt(unitName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        compositeItemViewModel = ViewModelProviders.of(this).get(CompositeItemViewModel.class);
        compositeItemViewModel.getAllCompositeItems().observe(this, new Observer<List<CompositeItem>>() {
            @Override
            public void onChanged(@Nullable final List<CompositeItem> compositeItems) {
                if(compositeItems == null){
                    return;
                }

                compositeItemList = compositeItems;

                List<String> compositeItemStrList = new ArrayList<>();
                for(CompositeItem compositeItem : compositeItems){
                    compositeItemStrList.add(compositeItem.getName());
                }

                int selectedPosition = -1;
                selectedPosition = compositeItemStrList.indexOf(selectedCompositeItem);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, compositeItemStrList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                compositeItemSpinner.setAdapter(dataAdapter);
                if(selectedPosition != -1) {
                    compositeItemSpinner.setSelection(selectedPosition);
                }
            }
        });

        compositeItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String compositeItemName = parent.getItemAtPosition(position).toString();
                compositeItemSpinner.setPrompt(compositeItemName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(itemSpinner.getPrompt()) || TextUtils.isEmpty(amountView.getText())
                    || TextUtils.isEmpty(unitSpinner.getPrompt()) || TextUtils.isEmpty(compositeItemSpinner.getPrompt())){
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    replyIntent.putExtra(ApplicationStrings.COMPONENT_ITEM_ID, getItemIdByName(itemSpinner.getPrompt().toString()));
                    replyIntent.putExtra(ApplicationStrings.COMPONENT_ITEM, itemSpinner.getPrompt().toString());
                    replyIntent.putExtra(ApplicationStrings.COMPONENT_AMOUNT, Float.parseFloat(amountView.getText().toString()));
                    replyIntent.putExtra(ApplicationStrings.COMPONENT_UNIT_ID, getUnitIdByName(unitSpinner.getPrompt().toString()));
                    replyIntent.putExtra(ApplicationStrings.COMPONENT_UNIT, unitSpinner.getPrompt().toString());
                    replyIntent.putExtra(ApplicationStrings.COMPONENT_COMPOSITE_ITEM_ID, getCompositeItemIdByName(compositeItemSpinner.getPrompt().toString()));
                    replyIntent.putExtra(ApplicationStrings.COMPONENT_COMPOSITE_ITEM, compositeItemSpinner.getPrompt().toString());

                    int id = getIntent().getIntExtra(ApplicationStrings.COMPONENT_ID, -1);
                    if (id != -1) {
                        replyIntent.putExtra(ApplicationStrings.COMPONENT_ID, id);
                    }

                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }

    private int getItemIdByName(String name){
        for(Item unit : itemList){
            if(unit.getName().equals(name)){
                return unit.getId();
            }
        }
        return -1;
    }

    private int getUnitIdByName(String name){
        for(Unit unit : unitList){
            if(unit.getName().equals(name)){
                return unit.getId();
            }
        }
        return -1;
    }

    private int getCompositeItemIdByName(String name){
        for(CompositeItem compositeItem : compositeItemList){
            if(compositeItem.getName().equals(name)){
                return compositeItem.getId();
            }
        }
        return -1;
    }
}
