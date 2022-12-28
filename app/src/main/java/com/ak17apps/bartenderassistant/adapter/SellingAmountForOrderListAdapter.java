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
import com.ak17apps.bartenderassistant.entity.Orderable;
import com.ak17apps.bartenderassistant.entity.SellingAmount;

import java.util.ArrayList;
import java.util.List;

public class SellingAmountForOrderListAdapter extends RecyclerView.Adapter<SellingAmountForOrderListAdapter.SellingAmountViewHolder> {
    private final LayoutInflater inflater;
    private int multiplier = 1;
    private TextView sellingAmountNameView;
    private List<Orderable> orderables = new ArrayList<>();

    public SellingAmountForOrderListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    class SellingAmountViewHolder extends RecyclerView.ViewHolder {
        private TextView sellingAmountNameTextView;
        private final Button addButton;
        private final Button increaseAmountButton;
        private final Button decreaseAmountButton;

        private SellingAmountViewHolder(final View categoryView) {
            super(categoryView);
            sellingAmountNameTextView = categoryView.findViewById(R.id.name_tv);
            sellingAmountNameView = sellingAmountNameTextView;
            addButton = categoryView.findViewById(R.id.button_add);
            increaseAmountButton = categoryView.findViewById(R.id.button_increase_amount);
            decreaseAmountButton = categoryView.findViewById(R.id.button_decrease_amount);
        }
    }

    public TextView getSellingAmountNameView(){
        return sellingAmountNameView;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier, Orderable orderable) {
        if(this.multiplier == 1 && multiplier < this.multiplier){
            return;
        }
        this.multiplier = multiplier;

        if(orderable instanceof SellingAmount){
            SellingAmount sa = (SellingAmount) orderable;
            sellingAmountNameView.setText(createSellingAmountText(sa.getItem(), sa.getAmount(), sa.getUnit(), sa.getPrice()));
        }else{
            CompositeItem ci = (CompositeItem) orderable;
            sellingAmountNameView.setText(createCompositeItemText(ci.getName(), ci.getPrice()));
        }
    }

    @Override
    public SellingAmountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_selling_amount_for_order, parent, false);
        return new SellingAmountViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SellingAmountViewHolder holder, int position) {
        if(orderables != null){
            Orderable orderable = orderables.get(position);

            if(orderable instanceof SellingAmount){
                SellingAmount sellingAmount = (SellingAmount) orderable;
                holder.sellingAmountNameTextView.setText(createSellingAmountText(sellingAmount.getItem(), sellingAmount.getAmount(), sellingAmount.getUnit(), sellingAmount.getPrice()));
            }else{  //CompositeItem
                CompositeItem compositeItem = (CompositeItem) orderable;
                holder.sellingAmountNameTextView.setText(createCompositeItemText(compositeItem.getName(), compositeItem.getPrice()));
            }
        }else{
            holder.sellingAmountNameTextView.setText("No Item");
        }
    }

    private String createMultiplierStr(){
        return multiplier > 1 ? " x" + multiplier : "";
    }

    private String createSellingAmountText(String item, float amount, String unit, float price){
        return item + " " + amount + " " + unit + " - " + price + " Ft" + createMultiplierStr();
    }

    private String createCompositeItemText(String name, float price){
        return name + " - " + price + " Ft" + createMultiplierStr();
    }

    public void addOrderables(List<Orderable> orderables){
        this.orderables.addAll(orderables);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (orderables != null) {
            return orderables.size();
        }else {
            return 0;
        }
    }

    public Orderable getOrderableAt(int position) {
        return orderables.get(position);
    }
}