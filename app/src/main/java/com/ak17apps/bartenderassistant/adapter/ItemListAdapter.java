package com.ak17apps.bartenderassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ak17apps.bartenderassistant.R;
import com.ak17apps.bartenderassistant.entity.Item;
import com.ak17apps.bartenderassistant.viewmodel.ItemViewModel;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {
private ItemViewModel itemViewModel;

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemNameView, itemStoredAmountView, itemSellableIndividuallyView, itemMaxAmountView;
        private final Button editButton, deleteButton;

        private ItemViewHolder(final View itemView) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.name_tv);
            itemStoredAmountView = itemView.findViewById(R.id.stored_amount_tv);
            itemSellableIndividuallyView = itemView.findViewById(R.id.sellable_individually_tv);
            itemMaxAmountView = itemView.findViewById(R.id.max_amount_tv);

            editButton = itemView.findViewById(R.id.button_edit);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }

    private final LayoutInflater inflater;
    private List<Item> items; // Cached copy of words

    public ItemListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        if (items != null) {
            Item item = items.get(position);
            holder.itemNameView.setText("Név: " + item.getName());
            holder.itemStoredAmountView.setText("Mennyiség: " + item.getStoredAmount() + " " + item.getPackagingUnit());
            holder.itemMaxAmountView.setText("Max mennyiség: " + item.getMaxAmount() + " " + item.getPackagingUnit());
            holder.itemSellableIndividuallyView.setText("Egyedileg értékesíthető: " + (item.isSellableIndividually() ? "Igen" : "Nem"));
        } else {
            holder.itemNameView.setText("No Item");
        }
    }

    public void setItems(List<Item> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }else {
            return 0;
        }
    }

    public Item getItemAt(int position) {
        return items.get(position);
    }
}
