package com.ak17apps.bartenderassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ak17apps.bartenderassistant.R;
import com.ak17apps.bartenderassistant.entity.CompositeItem;
import com.ak17apps.bartenderassistant.entity.Item;
import com.ak17apps.bartenderassistant.viewmodel.CompositeItemViewModel;

import java.util.List;

public class CompositeItemListAdapter extends RecyclerView.Adapter<CompositeItemListAdapter.CompositeItemViewHolder> {
    private CompositeItemViewModel compositeItemViewModel;
    private final LayoutInflater inflater;
    private List<CompositeItem> compositeItems; // Cached copy of words
    public CompositeItemListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    class CompositeItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView compositeItemNameView, compositeItemPriceView, compositeItemCategoryView;
        private final Button editButton, deleteButton;

        private CompositeItemViewHolder(final View compositeItemView) {
            super(compositeItemView);
            compositeItemNameView = compositeItemView.findViewById(R.id.name_tv);
            compositeItemPriceView = compositeItemView.findViewById(R.id.price_tv);
            compositeItemCategoryView = compositeItemView.findViewById(R.id.category_tv);

            editButton = compositeItemView.findViewById(R.id.button_edit);
            deleteButton = compositeItemView.findViewById(R.id.button_delete);
        }
    }

    @Override
    public CompositeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_composite_item, parent, false);
        return new CompositeItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CompositeItemViewHolder holder, int position) {
        if (compositeItems != null) {
            CompositeItem compositeItem = compositeItems.get(position);
            holder.compositeItemNameView.setText("Név: " + compositeItem.getName());
            holder.compositeItemPriceView.setText("Ár: " + compositeItem.getPrice() + " Ft");
            holder.compositeItemCategoryView.setText("Kategória: " + compositeItem.getCategory());
        } else {
            holder.compositeItemNameView.setText("No Item");
        }
    }

    public void setCompositeItems(List<CompositeItem> compositeItems){
        this.compositeItems = compositeItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (compositeItems != null) {
            return compositeItems.size();
        }else {
            return 0;
        }
    }

    public CompositeItem getCompositeItemAt(int position) {
        return compositeItems.get(position);
    }
}
