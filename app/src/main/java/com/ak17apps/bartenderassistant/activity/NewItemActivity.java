package com.ak17apps.bartenderassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ak17apps.bartenderassistant.R;
import com.ak17apps.bartenderassistant.entity.Unit;
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;
import com.ak17apps.bartenderassistant.viewmodel.UnitViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewItemActivity extends AppCompatActivity {
    private EditText editItemNameView, editItemStoredAmountView, editItemMaxAmountView;
    private Spinner editItemPackagingUnitSpinner;
    private CheckBox editItemSellableIndividuallyCB;
    private UnitViewModel unitViewModel;
    private AppCompatActivity activity = this;
    private List<Unit> unitList;
    private String selectedUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        editItemNameView = findViewById(R.id.edit_item_name);
        editItemStoredAmountView = findViewById(R.id.edit_item_stored_amount);
        editItemPackagingUnitSpinner = findViewById(R.id.edit_item_packaging_unit);
        editItemSellableIndividuallyCB = findViewById(R.id.edit_item_sellable_individually);
        editItemMaxAmountView = findViewById(R.id.edit_item_max_amount);

        Intent intent = getIntent();
        if (intent.hasExtra(ApplicationStrings.ITEM_ID)) {
            setTitle(getResources().getString(R.string.edit_item));
            editItemNameView.setText(intent.getStringExtra(ApplicationStrings.ITEM_NAME));
            editItemStoredAmountView.setText(intent.getStringExtra(ApplicationStrings.ITEM_STORED_AMOUNT));
            editItemMaxAmountView.setText(intent.getStringExtra(ApplicationStrings.ITEM_MAX_AMOUNT));
            selectedUnit = intent.getStringExtra(ApplicationStrings.ITEM_PACKAGING_UNIT);
            editItemSellableIndividuallyCB.setChecked(intent.getBooleanExtra(ApplicationStrings.ITEM_SELLABLE_INDIVIDUALLY, true));
        } else {
            setTitle(getResources().getString(R.string.new_item));
        }

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
                editItemPackagingUnitSpinner.setAdapter(dataAdapter);
                if(selectedPosition != -1) {
                    editItemPackagingUnitSpinner.setSelection(selectedPosition);
                }
            }
        });

        editItemPackagingUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String unitName = parent.getItemAtPosition(position).toString();
                editItemPackagingUnitSpinner.setPrompt(unitName);
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
                if (TextUtils.isEmpty(editItemNameView.getText()) || TextUtils.isEmpty(editItemStoredAmountView.getText())
                    || TextUtils.isEmpty(editItemPackagingUnitSpinner.getPrompt()) || TextUtils.isEmpty(editItemMaxAmountView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    replyIntent.putExtra(ApplicationStrings.ITEM_NAME, editItemNameView.getText().toString());
                    replyIntent.putExtra(ApplicationStrings.ITEM_STORED_AMOUNT, Float.parseFloat(editItemStoredAmountView.getText().toString()));
                    replyIntent.putExtra(ApplicationStrings.ITEM_PACKAGING_UNIT_ID, getUnitIdByName(editItemPackagingUnitSpinner.getPrompt().toString()));
                    replyIntent.putExtra(ApplicationStrings.ITEM_PACKAGING_UNIT, editItemPackagingUnitSpinner.getPrompt().toString());
                    replyIntent.putExtra(ApplicationStrings.ITEM_SELLABLE_INDIVIDUALLY, editItemSellableIndividuallyCB.isChecked());
                    replyIntent.putExtra(ApplicationStrings.ITEM_MAX_AMOUNT, Float.parseFloat(editItemMaxAmountView.getText().toString()));

                    int id = getIntent().getIntExtra(ApplicationStrings.ITEM_ID, -1);
                    if (id != -1) {
                        replyIntent.putExtra(ApplicationStrings.ITEM_ID, id);
                    }

                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }

    private int getUnitIdByName(String name){
        for(Unit unit : unitList){
            if(unit.getName().equals(name)){
                return unit.getId();
            }
        }
        return -1;
    }
}
