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
import com.ak17apps.bartenderassistant.entity.Unit;
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;
import com.ak17apps.bartenderassistant.viewmodel.UnitViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewUnitExchangeActivity extends AppCompatActivity {
    private EditText fromUnitValue, toUnitValue;
    private Spinner fromUnitSpinner, toUnitSpinner;
    private UnitViewModel unitViewModel;
    private AppCompatActivity activity = this;
    private List<Unit> unitList;
    private String selectedFromUnit, selectedToUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_unit_exchange);

        fromUnitValue = findViewById(R.id.fromUnitTV);
        toUnitValue = findViewById(R.id.toUnitTV);
        fromUnitSpinner = findViewById(R.id.fromUnitSP);
        toUnitSpinner = findViewById(R.id.toUnitSP);

        Intent intent = getIntent();
        if (intent.hasExtra(ApplicationStrings.UNIT_EXCHANGE_ID)) {
            setTitle(getResources().getString(R.string.edit_unit_exchange));
            fromUnitValue.setText(intent.getStringExtra(ApplicationStrings.UNIT_EXCHANGE_FROM_VALUE));
            toUnitValue.setText(intent.getStringExtra(ApplicationStrings.UNIT_EXCHANGE_TO_VALUE));
            selectedFromUnit = intent.getStringExtra(ApplicationStrings.UNIT_EXCHANGE_FROM_UNIT);
            selectedToUnit = intent.getStringExtra(ApplicationStrings.UNIT_EXCHANGE_TO_UNIT);
        } else {
            setTitle(getResources().getString(R.string.new_unit_exchange));
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

                int fromPos = -1;
                fromPos = unitStrList.indexOf(selectedFromUnit);

                ArrayAdapter<String> toAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, unitStrList);
                toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                fromUnitSpinner.setAdapter(toAdapter);
                if(fromPos != -1) {
                    fromUnitSpinner.setSelection(fromPos);
                }

                int toPos = -1;
                toPos = unitStrList.indexOf(selectedToUnit);

                ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, unitStrList);
                fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                toUnitSpinner.setAdapter(fromAdapter);
                if(toPos != -1) {
                    toUnitSpinner.setSelection(toPos);
                }
            }
        });

        fromUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String unitName = parent.getItemAtPosition(position).toString();
                fromUnitSpinner.setPrompt(unitName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        toUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String unitName = parent.getItemAtPosition(position).toString();
                toUnitSpinner.setPrompt(unitName);
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
                if (TextUtils.isEmpty(fromUnitValue.getText()) || TextUtils.isEmpty(toUnitValue.getText())
                    || TextUtils.isEmpty(fromUnitSpinner.getPrompt()) || TextUtils.isEmpty(toUnitSpinner.getPrompt())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    replyIntent.putExtra(ApplicationStrings.UNIT_EXCHANGE_FROM_UNIT_ID, getUnitIdByName(fromUnitSpinner.getPrompt().toString()));
                    replyIntent.putExtra(ApplicationStrings.UNIT_EXCHANGE_FROM_UNIT, fromUnitSpinner.getPrompt().toString());
                    replyIntent.putExtra(ApplicationStrings.UNIT_EXCHANGE_FROM_VALUE, Float.parseFloat(fromUnitValue.getText().toString()));
                    replyIntent.putExtra(ApplicationStrings.UNIT_EXCHANGE_TO_UNIT_ID, getUnitIdByName(toUnitSpinner.getPrompt().toString()));
                    replyIntent.putExtra(ApplicationStrings.UNIT_EXCHANGE_TO_UNIT, toUnitSpinner.getPrompt().toString());
                    replyIntent.putExtra(ApplicationStrings.UNIT_EXCHANGE_TO_VALUE, Float.parseFloat(toUnitValue.getText().toString()));

                    int id = getIntent().getIntExtra(ApplicationStrings.UNIT_EXCHANGE_ID, -1);
                    if (id != -1) {
                        replyIntent.putExtra(ApplicationStrings.UNIT_EXCHANGE_ID, id);
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
