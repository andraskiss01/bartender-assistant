package com.ak17apps.bartenderassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ak17apps.bartenderassistant.R;
import com.ak17apps.bartenderassistant.adapter.RecyclerItemClickListener;
import com.ak17apps.bartenderassistant.adapter.SellingAmountForOrderListAdapter;
import com.ak17apps.bartenderassistant.entity.CompositeItem;
import com.ak17apps.bartenderassistant.entity.Orderable;
import com.ak17apps.bartenderassistant.entity.SellingAmount;
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;
import com.ak17apps.bartenderassistant.utils.CategoryHierarchy;
import com.ak17apps.bartenderassistant.utils.OrderableTypes;
import com.ak17apps.bartenderassistant.viewmodel.CompositeItemViewModel;
import com.ak17apps.bartenderassistant.viewmodel.SellingAmountViewModel;

import java.util.List;

public class NewOrderWithSellingAmountsActivity extends AppCompatActivity {
    private SellingAmountViewModel sellingAmountViewModel;
    private CompositeItemViewModel compositeItemViewModel;
    private Button addButton;
    private Button increaseAmountButton;
    private Button decreaseAmountButton;
    private int parentCategoryId;
    private String parentCategory;
    private int boardId;
    private String boardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_with_selling_amounts);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        Intent intent = getIntent();
        parentCategoryId = intent.getIntExtra(ApplicationStrings.PARENT_CATEGORY_ID, -1);
        parentCategory = intent.getStringExtra(ApplicationStrings.PARENT_CATEGORY);
        boardId = intent.getIntExtra(ApplicationStrings.BOARD_ID, -1);
        boardName = intent.getStringExtra(ApplicationStrings.BOARD_NAME);
        setTitle(parentCategory);

        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final SellingAmountForOrderListAdapter adapter = new SellingAmountForOrderListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sellingAmountViewModel = ViewModelProviders.of(this).get(SellingAmountViewModel.class);
        sellingAmountViewModel.setCategoryId(parentCategoryId);
        sellingAmountViewModel.getSellingAmountsOfCategory().observe(this, new Observer<List<SellingAmount>>() {
            @Override
            public void onChanged(@Nullable final List<SellingAmount> sellingAmounts) {
                adapter.addOrderables((List<Orderable>)(List<?>) sellingAmounts);
            }
        });

        compositeItemViewModel = ViewModelProviders.of(this).get(CompositeItemViewModel.class);
        compositeItemViewModel.setCategoryId(parentCategoryId);
        compositeItemViewModel.getCompositeItemsOfCategory().observe(this, new Observer<List<CompositeItem>>() {
            @Override
            public void onChanged(@Nullable final List<CompositeItem> compositeItems) {
                adapter.addOrderables((List<Orderable>)(List<?>) compositeItems);
            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, final int position) {
                        addButton = v.findViewById(R.id.button_add);
                        addButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String multiplierStr = (adapter.getMultiplier() != 1 ? adapter.getMultiplier() + "x " : "");
                                Orderable orderable = adapter.getOrderableAt(position);
                                Intent intent = new Intent();

                                if(orderable instanceof SellingAmount){
                                    SellingAmount sellingAmount = (SellingAmount) orderable;
                                    intent.putExtra(ApplicationStrings.ORDER_ORDERABLE_ID, sellingAmount.getId());
                                    intent.putExtra(ApplicationStrings.ORDER_ORDERABLE,  multiplierStr + sellingAmount.getItem() + " " +
                                            sellingAmount.getAmount() + " " + sellingAmount.getUnit() + " " + sellingAmount.getPrice() + " Ft");
                                    intent.putExtra(ApplicationStrings.ORDER_PRICE, sellingAmount.getPrice() * adapter.getMultiplier());
                                    intent.putExtra(ApplicationStrings.ORDER_ORDERABLE_TYPE, OrderableTypes.SELLING_AMOUNT.name());
                                }else{  //CompositeItem
                                    CompositeItem compositeItem = (CompositeItem) orderable;
                                    intent.putExtra(ApplicationStrings.ORDER_ORDERABLE_ID, compositeItem.getId());
                                    intent.putExtra(ApplicationStrings.ORDER_ORDERABLE, multiplierStr + compositeItem.getName() + " " + compositeItem.getPrice() + " Ft");
                                    intent.putExtra(ApplicationStrings.ORDER_PRICE, compositeItem.getPrice() * adapter.getMultiplier());
                                    intent.putExtra(ApplicationStrings.ORDER_ORDERABLE_TYPE, OrderableTypes.COMPOSITE_ITEM.name());
                                }

                                intent.putExtra(ApplicationStrings.ORDER_QUANTITY, adapter.getMultiplier());
                                intent.putExtra(ApplicationStrings.ORDER_BOARD_ID, boardId);
                                intent.putExtra(ApplicationStrings.ORDER_BOARD, boardName);
                                setResult(RESULT_OK, intent);
                                finish();
                                overridePendingTransition(0, 0);
                            }
                        });

                        increaseAmountButton = v.findViewById(R.id.button_increase_amount);
                        increaseAmountButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Orderable orderable = adapter.getOrderableAt(position);
                                adapter.setMultiplier(adapter.getMultiplier() + 1, orderable);
                            }
                        });

                        decreaseAmountButton = v.findViewById(R.id.button_decrease_amount);
                        decreaseAmountButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Orderable orderable = adapter.getOrderableAt(position);
                                adapter.setMultiplier(adapter.getMultiplier() - 1, orderable);
                            }
                        });
                    }
                })
        );
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
            intent.putExtra(ApplicationStrings.PARENT_CATEGORY, CategoryHierarchy.categoryNames.size() > 0 ? CategoryHierarchy.categoryNames.get(CategoryHierarchy.categoryNames.size() - 1) : "");
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
