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
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;
import com.ak17apps.bartenderassistant.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewCategoryActivity extends AppCompatActivity {
    private EditText editCategoryNameView;
    private Spinner editParentCategorySpinner;
    private CategoryViewModel categoryViewModel;
    private AppCompatActivity activity = this;
    private List<Category> categoryList;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        editCategoryNameView = findViewById(R.id.edit_category_name);
        editParentCategorySpinner = findViewById(R.id.edit_category_parent_category);

        Intent intent = getIntent();
        if (intent.hasExtra(ApplicationStrings.CATEGORY_ID)) {
            setTitle(getResources().getString(R.string.edit_category));
            editCategoryNameView.setText(intent.getStringExtra(ApplicationStrings.CATEGORY_NAME));
            selectedCategory = intent.getStringExtra(ApplicationStrings.PARENT_CATEGORY);
        } else {
            setTitle(getResources().getString(R.string.new_category));
        }

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable final List<Category> categories) {
                if(categories == null){
                    return;
                }

                categories.add(new Category("", -1, ""));
                categoryList = categories;

                List<String> categoryStrList = new ArrayList<>();
                for(Category category : categories){
                    categoryStrList.add(category.getName());
                }

                int selectedPosition = categoryStrList.indexOf(selectedCategory);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, categoryStrList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editParentCategorySpinner.setAdapter(dataAdapter);
                editParentCategorySpinner.setSelection(selectedPosition);
            }
        });

        editParentCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryName = parent.getItemAtPosition(position).toString();
                editParentCategorySpinner.setPrompt(categoryName);
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
                if (TextUtils.isEmpty(editCategoryNameView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    replyIntent.putExtra(ApplicationStrings.CATEGORY_NAME, editCategoryNameView.getText().toString());
                    replyIntent.putExtra(ApplicationStrings.PARENT_CATEGORY_ID, editParentCategorySpinner.getPrompt() != null ? getCategoryIdByName(editParentCategorySpinner.getPrompt().toString()) : -1);
                    replyIntent.putExtra(ApplicationStrings.PARENT_CATEGORY, editParentCategorySpinner.getPrompt() != null ? editParentCategorySpinner.getPrompt().toString() : "");

                    int id = getIntent().getIntExtra(ApplicationStrings.CATEGORY_ID, -1);
                    if (id != -1) {
                        replyIntent.putExtra(ApplicationStrings.CATEGORY_ID, id);
                    }

                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }

    private int getCategoryIdByName(String name){
        if(name.equals("")){
            return -1;
        }

        for(Category category : categoryList){
            if(category.getName().equals(name)){
                return category.getId();
            }
        }
        return -1;
    }
}
