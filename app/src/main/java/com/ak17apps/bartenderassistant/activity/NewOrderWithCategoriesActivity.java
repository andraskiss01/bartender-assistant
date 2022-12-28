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
import com.ak17apps.bartenderassistant.adapter.CategoryForOrderListAdapter;
import com.ak17apps.bartenderassistant.adapter.RecyclerItemClickListener;
import com.ak17apps.bartenderassistant.entity.Category;
import com.ak17apps.bartenderassistant.entity.Order;
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;
import com.ak17apps.bartenderassistant.utils.CategoryHierarchy;
import com.ak17apps.bartenderassistant.viewmodel.CategoryViewModel;
import com.ak17apps.bartenderassistant.viewmodel.OrderViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewOrderWithCategoriesActivity extends AppCompatActivity {
    private CategoryViewModel categoryViewModel;
    private OrderViewModel orderViewModel;
    private Button openButton;
    private int parentCategoryId;
    private String parentCategory;
    private int boardId;
    private String boardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_with_categories);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        Intent intent = getIntent();
        parentCategoryId = intent.getIntExtra(ApplicationStrings.PARENT_CATEGORY_ID, -1);
        parentCategory = intent.getStringExtra(ApplicationStrings.PARENT_CATEGORY);
        boardId = intent.getIntExtra(ApplicationStrings.BOARD_ID, -1);
        boardName = intent.getStringExtra(ApplicationStrings.BOARD_NAME);
        if(parentCategoryId == -1){
            setTitle(getResources().getString(R.string.new_order) + " - " + boardName);
            CategoryHierarchy.categoryIds = new ArrayList<>();
            CategoryHierarchy.categoryNames = new ArrayList<>();
        }else{
            setTitle(parentCategory);
            boolean contains = false;
            if(CategoryHierarchy.categoryIds != null){
                for (int i : CategoryHierarchy.categoryIds) {
                    if (i == parentCategoryId) {
                        contains = true;
                        break;
                    }
                }
            }else{
                CategoryHierarchy.categoryIds = new ArrayList<>();
                CategoryHierarchy.categoryNames = new ArrayList<>();
            }
            if(!contains) {
                CategoryHierarchy.categoryIds.add(CategoryHierarchy.categoryIds.size(), parentCategoryId);
                CategoryHierarchy.categoryNames.add(CategoryHierarchy.categoryNames.size(), parentCategory);
            }
        }

        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final CategoryForOrderListAdapter adapter = new CategoryForOrderListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryViewModel.setParentCategoryId(parentCategoryId);
        categoryViewModel.getCategoriesOfParentCategory().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable final List<Category> categories) {
                if(categories != null && !categories.isEmpty()) {
                    adapter.setCategories(categories);
                }else{
                    showSellingAmounts();
                }
            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, final int position) {
                        openButton = v.findViewById(R.id.button_open);
                        openButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(NewOrderWithCategoriesActivity.this, NewOrderWithCategoriesActivity.class);
                                intent.putExtra(ApplicationStrings.PARENT_CATEGORY_ID, adapter.getCategoryAt(position).getId());
                                intent.putExtra(ApplicationStrings.PARENT_CATEGORY, adapter.getCategoryAt(position).getName());
                                intent.putExtra(ApplicationStrings.BOARD_ID, boardId);
                                intent.putExtra(ApplicationStrings.BOARD_NAME, boardName);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(0, 0);
                            }
                        });
                    }
                })
        );
    }

    private void showSellingAmounts(){
        Intent intent = new Intent(this, NewOrderWithSellingAmountsActivity.class);
        intent.putExtra(ApplicationStrings.PARENT_CATEGORY_ID, parentCategoryId);
        intent.putExtra(ApplicationStrings.PARENT_CATEGORY, parentCategory);
        intent.putExtra(ApplicationStrings.BOARD_ID, boardId);
        intent.putExtra(ApplicationStrings.BOARD_NAME, boardName);
        startActivityForResult(intent, 1);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);

            int sellingAmountId = intent.getIntExtra(ApplicationStrings.ORDER_ORDERABLE_ID, -1);
            String sellingAmount = intent.getStringExtra(ApplicationStrings.ORDER_ORDERABLE);
            float price = intent.getFloatExtra(ApplicationStrings.ORDER_PRICE, 0F);
            int boardId = intent.getIntExtra(ApplicationStrings.ORDER_BOARD_ID, -1);
            String board = intent.getStringExtra(ApplicationStrings.ORDER_BOARD);
            int quantity = intent.getIntExtra(ApplicationStrings.ORDER_QUANTITY, 1);
            String orderableType = intent.getStringExtra(ApplicationStrings.ORDER_ORDERABLE_TYPE);

            Order order = new Order(boardId, board, quantity, sellingAmountId, sellingAmount, price, orderableType, false);
            orderViewModel.insert(order);

            Intent i = new Intent(this, OrderActivity.class);
            i.putExtra(ApplicationStrings.BOARD_ID, boardId);
            i.putExtra(ApplicationStrings.BOARD_NAME, boardName);
            startActivity(i);
            finish();
            overridePendingTransition(0, 0);
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent;
        if(CategoryHierarchy.categoryIds.size() > 0) {
            CategoryHierarchy.categoryIds.remove(CategoryHierarchy.categoryIds.size() - 1);
            CategoryHierarchy.categoryNames.remove(CategoryHierarchy.categoryNames.size() - 1);

            intent = new Intent(this, NewOrderWithCategoriesActivity.class);
            intent.putExtra(ApplicationStrings.PARENT_CATEGORY_ID, CategoryHierarchy.categoryIds.size() > 0 ? CategoryHierarchy.categoryIds.get(CategoryHierarchy.categoryIds.size() - 1) : -1);
            intent.putExtra(ApplicationStrings.PARENT_CATEGORY, CategoryHierarchy.categoryNames.size() > 0 ?  CategoryHierarchy.categoryNames.get(CategoryHierarchy.categoryNames.size() - 1) : "");
        }else{
            intent = new Intent(this, OrderActivity.class);
            intent.putExtra(ApplicationStrings.PARENT_CATEGORY_ID, -1);
            intent.putExtra(ApplicationStrings.PARENT_CATEGORY, "");
        }

        intent.putExtra(ApplicationStrings.BOARD_ID, boardId);
        intent.putExtra(ApplicationStrings.BOARD_NAME, boardName);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }
}
