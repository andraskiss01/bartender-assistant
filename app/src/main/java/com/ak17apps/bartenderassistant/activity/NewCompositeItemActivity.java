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
import com.ak17apps.bartenderassistant.entity.Unit;
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;
import com.ak17apps.bartenderassistant.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewCompositeItemActivity extends AppCompatActivity {
    private EditText editCompositeItemNameView, editCompositeItemPriceView;
    private Spinner editCompositeItemCategorySpinner;
    private CategoryViewModel categoryViewModel;
    private AppCompatActivity activity = this;
    private List<Category> categoryList;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_composite_item);

        editCompositeItemNameView = findViewById(R.id.edit_composite_item_name);
        editCompositeItemPriceView = findViewById(R.id.edit_composite_item_price);
        editCompositeItemCategorySpinner = findViewById(R.id.edit_composite_item_category);

        Intent intent = getIntent();
        if (intent.hasExtra(ApplicationStrings.COMPOSITE_ITEM_ID)) {
            setTitle(getResources().getString(R.string.edit_composite_item));
            editCompositeItemNameView.setText(intent.getStringExtra(ApplicationStrings.COMPOSITE_ITEM_NAME));
            editCompositeItemPriceView.setText(intent.getStringExtra(ApplicationStrings.COMPOSITE_ITEM_PRICE));
            selectedCategory = intent.getStringExtra(ApplicationStrings.COMPOSITE_ITEM_CATEGORY);
        } else {
            setTitle(getResources().getString(R.string.new_composite_item));
        }

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
                editCompositeItemCategorySpinner.setAdapter(dataAdapter);
                if(selectedPosition != -1) {
                    editCompositeItemCategorySpinner.setSelection(selectedPosition);
                }
            }
        });

        editCompositeItemCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryName = parent.getItemAtPosition(position).toString();
                editCompositeItemCategorySpinner.setPrompt(categoryName);
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
                if (TextUtils.isEmpty(editCompositeItemNameView.getText()) || TextUtils.isEmpty(editCompositeItemPriceView.getText())
                    || TextUtils.isEmpty(editCompositeItemCategorySpinner.getPrompt())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    replyIntent.putExtra(ApplicationStrings.COMPOSITE_ITEM_NAME, editCompositeItemNameView.getText().toString());
                    replyIntent.putExtra(ApplicationStrings.COMPOSITE_ITEM_PRICE, Float.parseFloat(editCompositeItemPriceView.getText().toString()));
                    replyIntent.putExtra(ApplicationStrings.COMPOSITE_ITEM_CATEGORY_ID, getCompositeItemIdByName(editCompositeItemCategorySpinner.getPrompt().toString()));
                    replyIntent.putExtra(ApplicationStrings.COMPOSITE_ITEM_CATEGORY, editCompositeItemCategorySpinner.getPrompt().toString());

                    int id = getIntent().getIntExtra(ApplicationStrings.COMPOSITE_ITEM_ID, -1);
                    if (id != -1) {
                        replyIntent.putExtra(ApplicationStrings.COMPOSITE_ITEM_ID, id);
                    }

                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }

    private int getCompositeItemIdByName(String name){
        for(Category category : categoryList){
            if(category.getName().equals(name)){
                return category.getId();
            }
        }
        return -1;
    }
}
