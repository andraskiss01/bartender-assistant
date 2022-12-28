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
import com.ak17apps.bartenderassistant.entity.SellingAmount;
import com.ak17apps.bartenderassistant.viewmodel.SellingAmountViewModel;

import java.util.List;

public class SellingAmountListAdapter extends RecyclerView.Adapter<SellingAmountListAdapter.SellingAmountViewHolder> {
    private SellingAmountViewModel sellingAmountViewModel;
    private final LayoutInflater inflater;
    private List<SellingAmount> sellingAmounts; // Cached copy of words

    public SellingAmountListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    class SellingAmountViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemNameView, amountView, priceView, categoryNameView;
        private final Button editButton, deleteButton;

        private SellingAmountViewHolder(final View sellingAmountView) {
            super(sellingAmountView);
            itemNameView = sellingAmountView.findViewById(R.id.item_name_tv);
            amountView = sellingAmountView.findViewById(R.id.amount_tv);
            priceView = sellingAmountView.findViewById(R.id.price_tv);
            categoryNameView = sellingAmountView.findViewById(R.id.category_name_tv);

            editButton = sellingAmountView.findViewById(R.id.button_edit);
            deleteButton = sellingAmountView.findViewById(R.id.button_delete);
        }
    }

    @Override
    public SellingAmountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View sellingAmountView = inflater.inflate(R.layout.recyclerview_selling_amount, parent, false);
        return new SellingAmountViewHolder(sellingAmountView);
    }

    @Override
    public void onBindViewHolder(final SellingAmountViewHolder holder, int position) {
        if (sellingAmounts != null) {
            SellingAmount sellingAmount = sellingAmounts.get(position);
            holder.itemNameView.setText("Termék: " + sellingAmount.getItem());
            holder.amountView.setText("Mennyiség: " + sellingAmount.getAmount() + " " + sellingAmount.getUnit());
            holder.priceView.setText("Ár: " + sellingAmount.getPrice() + " Ft");
            holder.categoryNameView.setText("Kategória: " + sellingAmount.getCategory());
        } else {
            holder.itemNameView.setText("No Item");
        }
    }

    public void setSellingAmounts(List<SellingAmount> sellingAmounts){
        this.sellingAmounts = sellingAmounts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (sellingAmounts != null) {
            return sellingAmounts.size();
        }else {
            return 0;
        }
    }

    public SellingAmount getSellingAmountAt(int position) {
        return sellingAmounts.get(position);
    }
}
