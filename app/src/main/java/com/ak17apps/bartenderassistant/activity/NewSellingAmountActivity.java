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
import com.ak17apps.bartenderassistant.entity.Category;
import com.ak17apps.bartenderassistant.entity.Item;
import com.ak17apps.bartenderassistant.entity.Unit;
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;
import com.ak17apps.bartenderassistant.viewmodel.CategoryViewModel;
import com.ak17apps.bartenderassistant.viewmodel.ItemViewModel;
import com.ak17apps.bartenderassistant.viewmodel.UnitViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewSellingAmountActivity extends AppCompatActivity {
    private Spinner editItemSP, editUnitSP, editCategorySP;
    private EditText editAmountView, editPriceView;
    private ItemViewModel itemViewModel;
    private UnitViewModel unitViewModel;
    private CategoryViewModel categoryViewModel;
    private AppCompatActivity activity = this;
    private List<Item> itemList;
    private List<Unit> unitList;
    private List<Category> categoryList;
    private String selectedItem, selectedUnit, selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_selling_amount);

        editItemSP = findViewById(R.id.edit_selling_amount_item);
        editAmountView = findViewById(R.id.edit_selling_amount_amount);
        editUnitSP = findViewById(R.id.edit_selling_amount_unit);
        editPriceView = findViewById(R.id.edit_selling_amount_price);
        editCategorySP = findViewById(R.id.edit_selling_amount_category);

        Intent intent = getIntent();
        if (intent.hasExtra(ApplicationStrings.SELLING_AMOUNT_ID)) {
            setTitle(getResources().getString(R.string.edit_selling_amount));
            selectedItem = intent.getStringExtra(ApplicationStrings.SELLING_AMOUNT_ITEM);
            editAmountView.setText(intent.getStringExtra(ApplicationStrings.SELLING_AMOUNT_AMOUNT));
            selectedUnit = intent.getStringExtra(ApplicationStrings.SELLING_AMOUNT_UNIT);
            editPriceView.setText(intent.getStringExtra(ApplicationStrings.SELLING_AMOUNT_PRICE));
            selectedCategory = intent.getStringExtra(ApplicationStrings.SELLING_AMOUNT_CATEGORY);
        } else {
            setTitle(getResources().getString(R.string.new_selling_amount));
        }

        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        itemViewModel.getAllItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable final List<Item> items) {
                if(items == null){
                    return;
                }

                itemList = items;

                List<String> itemStrList = new ArrayList<>();
                for(Item item : items){
                    itemStrList.add(item.getName());
                }

                int selectedPosition = -1;
                selectedPosition = itemStrList.indexOf(selectedItem);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, itemStrList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editItemSP.setAdapter(dataAdapter);
                if(selectedPosition != -1) {
                    editItemSP.setSelection(selectedPosition);
                }
            }
        });

        editItemSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemName = parent.getItemAtPosition(position).toString();
                editItemSP.setPrompt(itemName);
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
                editUnitSP.setAdapter(dataAdapter);
                if(selectedPosition != -1) {
                    editUnitSP.setSelection(selectedPosition);
                }
            }
        });

        editUnitSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String unitName = parent.getItemAtPosition(position).toString();
                editUnitSP.setPrompt(unitName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable final List<Category> categories) {
                if(categories == null){
                    return;
                }

                categoryList = categories;

                List<String> categoryStrList = new ArrayList<>();
                for(Category category : categories){
                    categoryStrList.add(category.getName());
                }

                int selectedPosition = -1;
                selectedPosition = categoryStrList.indexOf(selectedCategory);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, categoryStrList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editCategorySP.setAdapter(dataAdapter);
                if(selectedPosition != -1) {
                    editCategorySP.setSelection(selectedPosition);
                }
            }
        });

        editCategorySP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryName = parent.getItemAtPosition(position).toString();
                editCategorySP.setPrompt(categoryName);
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
                if (TextUtils.isEmpty(editAmountView.getText()) || TextUtils.isEmpty(editPriceView.getText())
                        || TextUtils.isEmpty(editItemSP.getPrompt())
                        || TextUtils.isEmpty(editUnitSP.getPrompt())
                        || TextUtils.isEmpty(editCategorySP.getPrompt())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    replyIntent.putExtra(ApplicationStrings.SELLING_AMOUNT_ITEM_ID, getItemIdByName(editItemSP.getPrompt().toString()));
                    replyIntent.putExtra(ApplicationStrings.SELLING_AMOUNT_ITEM, editItemSP.getPrompt().toString());
                    replyIntent.putExtra(ApplicationStrings.SELLING_AMOUNT_AMOUNT, Float.parseFloat(editAmountView.getText().toString()));
                    replyIntent.putExtra(ApplicationStrings.SELLING_AMOUNT_UNIT_ID, getUnitIdByName(editUnitSP.getPrompt().toString()));
                    replyIntent.putExtra(ApplicationStrings.SELLING_AMOUNT_UNIT, editUnitSP.getPrompt().toString());
                    replyIntent.putExtra(ApplicationStrings.SELLING_AMOUNT_PRICE, Float.parseFloat(editPriceView.getText().toString()));
                    replyIntent.putExtra(ApplicationStrings.SELLING_AMOUNT_CATEGORY_ID, getCategoryIdByName(editCategorySP.getPrompt().toString()));
                    replyIntent.putExtra(ApplicationStrings.SELLING_AMOUNT_CATEGORY, editCategorySP.getPrompt().toString());

                    int id = getIntent().getIntExtra(ApplicationStrings.SELLING_AMOUNT_ID, -1);
                    if (id != -1) {
                        replyIntent.putExtra(ApplicationStrings.SELLING_AMOUNT_ID, id);
                    }

                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }

    private int getItemIdByName(String name){
        for(Item item : itemList){
            if(item.getName().equals(name)){
                return item.getId();
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

    private int getCategoryIdByName(String name){
        for(Category category : categoryList){
            if(category.getName().equals(name)){
                return category.getId();
            }
        }
        return -1;
    }
}
