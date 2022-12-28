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
import com.ak17apps.bartenderassistant.adapter.CategoryListAdapter;
import com.ak17apps.bartenderassistant.adapter.RecyclerItemClickListener;
import com.ak17apps.bartenderassistant.entity.Category;
import com.ak17apps.bartenderassistant.entity.CompositeItem;
import com.ak17apps.bartenderassistant.entity.SellingAmount;
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;
import com.ak17apps.bartenderassistant.viewmodel.CategoryViewModel;
import com.ak17apps.bartenderassistant.viewmodel.CompositeItemViewModel;
import com.ak17apps.bartenderassistant.viewmodel.SellingAmountViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_WORD_ACTIVITY_REQUEST_CODE = 2;

    private CategoryViewModel categoryViewModel;
    private CompositeItemViewModel compositeItemViewModel;
    private SellingAmountViewModel sellingAmountViewModel;
    private Button editButton, deleteButton;
    private List<CompositeItem> compositeItems;
    private List<SellingAmount> sellingAmounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setTitle(getResources().getString(R.string.categories));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final CategoryListAdapter adapter = new CategoryListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sellingAmountViewModel = ViewModelProviders.of(this).get(SellingAmountViewModel.class);

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable final List<Category> categories) {
                adapter.setCategories(categories);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, NewCategoryActivity.class);
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
                                checkCompositeItemsForDelete(adapter.getCategoryAt(position));
                            }
                        });

                        editButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CategoryActivity.this, NewCategoryActivity.class);
                                intent.putExtra(ApplicationStrings.CATEGORY_ID, adapter.getCategoryAt(position).getId());
                                intent.putExtra(ApplicationStrings.CATEGORY_NAME, adapter.getCategoryAt(position).getName());
                                intent.putExtra(ApplicationStrings.PARENT_CATEGORY_ID,  adapter.getCategoryAt(position).getParentCategoryId());
                                intent.putExtra(ApplicationStrings.PARENT_CATEGORY,  adapter.getCategoryAt(position).getParentCategory());
                                startActivityForResult(intent, EDIT_WORD_ACTIVITY_REQUEST_CODE);
                            }
                        });
                    }
                })
        );
    }

    private void checkCompositeItemsForDelete(final Category category){
        compositeItemViewModel = ViewModelProviders.of(this).get(CompositeItemViewModel.class);
        compositeItemViewModel.setCategoryId(category.getId());
        compositeItemViewModel.getNumberOfCompositeItemsOfCategory().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer numOfCompositeItems) {
                if(numOfCompositeItems == null || numOfCompositeItems == 0){
                    checkSellingAmountsForDelete(category);
                }else{
                    Toast.makeText(getApplicationContext(), "This category is referenced by other objects", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkSellingAmountsForDelete(final Category category){
        sellingAmountViewModel = ViewModelProviders.of(this).get(SellingAmountViewModel.class);
        sellingAmountViewModel.setCategoryId(category.getId());
        sellingAmountViewModel.getSellingAmountsOfCategory().observe(this, new Observer<List<SellingAmount>>() {
            @Override
            public void onChanged(@Nullable final List<SellingAmount> sellingAmounts) {
                if(sellingAmounts == null || sellingAmounts.isEmpty()){
                    categoryViewModel.delete(category);
                }else{
                    Toast.makeText(getApplicationContext(), "This category is referenced by other objects", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String name = intent.getStringExtra(ApplicationStrings.CATEGORY_NAME);
            int parentCategoryId = intent.getIntExtra(ApplicationStrings.PARENT_CATEGORY_ID, -1);
            String parentCategory = intent.getStringExtra(ApplicationStrings.PARENT_CATEGORY);

            Category category = new Category(name, parentCategoryId, parentCategory);
            categoryViewModel.insert(category);
        } else if (requestCode == EDIT_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            int id = intent.getIntExtra(ApplicationStrings.CATEGORY_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = intent.getStringExtra(ApplicationStrings.CATEGORY_NAME);
            int parentCategoryId = intent.getIntExtra(ApplicationStrings.PARENT_CATEGORY_ID, -1);
            String parentCategory = intent.getStringExtra(ApplicationStrings.PARENT_CATEGORY);

            Category category = new Category(name, parentCategoryId, parentCategory);
            category.setId(id);
            categoryViewModel.update(category);
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
