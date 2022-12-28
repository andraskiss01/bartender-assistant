package com.ak17apps.bartenderassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ak17apps.bartenderassistant.R;
import com.ak17apps.bartenderassistant.entity.Order;
import com.ak17apps.bartenderassistant.viewmodel.OrderViewModel;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {
    private OrderViewModel orderViewModel;
    private final LayoutInflater inflater;
    private List<Order> orders; // Cached copy of words
    private ViewGroup parent;

    public OrderListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView sellingAmountView;
        private final Button deleteButton, signButton, tickButton;

        private OrderViewHolder(final View orderView) {
            super(orderView);
            sellingAmountView = orderView.findViewById(R.id.selling_amount);
            deleteButton = orderView.findViewById(R.id.button_delete);
            signButton = orderView.findViewById(R.id.button_sign);
            tickButton = orderView.findViewById(R.id.button_tick);
        }
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View orderView = inflater.inflate(R.layout.recyclerview_order, parent, false);
        return new OrderViewHolder(orderView);
    }

    @Override
    public void onBindViewHolder(final OrderViewHolder holder, int position) {
        if (orders != null) {
            Order order = orders.get(position);
            holder.sellingAmountView.setText(order.getOrderable());
            holder.signButton.setText(order.isSigned() ? "X" : "");
        } else {
            holder.sellingAmountView.setText("No Item");
        }
    }

    public ViewGroup getParent(){
        return parent;
    }

    public void setOrders(List<Order> orders){
        this.orders = orders;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (orders != null) {
            return orders.size();
        }else {
            return 0;
        }
    }

    public Order getOrderAt(int position) {
        return orders.get(position);
    }
}
