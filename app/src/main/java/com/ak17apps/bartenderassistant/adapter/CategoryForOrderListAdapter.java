package com.ak17apps.bartenderassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ak17apps.bartenderassistant.R;
import com.ak17apps.bartenderassistant.entity.Category;
import com.ak17apps.bartenderassistant.viewmodel.CategoryViewModel;

import java.util.List;

public class CategoryForOrderListAdapter extends RecyclerView.Adapter<CategoryForOrderListAdapter.CategoryViewHolder> {
    private CategoryViewModel categoryViewModel;
    private final LayoutInflater inflater;
    private List<Category> categories; // Cached copy of words

    public CategoryForOrderListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryNameView;
        private final Button openButton;

        private CategoryViewHolder(final View categoryView) {
            super(categoryView);
            categoryNameView = categoryView.findViewById(R.id.name_tv);
            openButton = categoryView.findViewById(R.id.button_open);
        }
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_category_for_order, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, int position) {
        if (categories != null) {
            Category category = categories.get(position);
            holder.categoryNameView.setText(category.getName());
        } else {
            holder.categoryNameView.setText("No Item");
        }
    }

    public void setCategories(List<Category> categories){
        this.categories = categories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (categories != null) {
            return categories.size();
        }else {
            return 0;
        }
    }

    public Category getCategoryAt(int position) {
        return categories.get(position);
    }
}
