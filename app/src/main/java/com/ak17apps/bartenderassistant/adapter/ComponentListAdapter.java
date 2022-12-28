package com.ak17apps.bartenderassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ak17apps.bartenderassistant.R;
import com.ak17apps.bartenderassistant.entity.Component;
import com.ak17apps.bartenderassistant.viewmodel.ComponentViewModel;

import java.util.List;

public class ComponentListAdapter extends RecyclerView.Adapter<ComponentListAdapter.ComponentViewHolder> {
    private ComponentViewModel componentViewModel;
    private final LayoutInflater inflater;
    private List<Component> components; // Cached copy of words
    public ComponentListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    class ComponentViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemNameView, amountView, compositeItemView;
        private final Button editButton, deleteButton;

        private ComponentViewHolder(final View componentView) {
            super(componentView);
            itemNameView = componentView.findViewById(R.id.item_tv);
            amountView = componentView.findViewById(R.id.amount_tv);
            compositeItemView = componentView.findViewById(R.id.composite_item_tv);

            editButton = componentView.findViewById(R.id.button_edit);
            deleteButton = componentView.findViewById(R.id.button_delete);
        }
    }

    @Override
    public ComponentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View componentView = inflater.inflate(R.layout.recyclerview_component, parent, false);
        return new ComponentViewHolder(componentView);
    }

    @Override
    public void onBindViewHolder(final ComponentViewHolder holder, int position) {
        if (components != null) {
            Component component = components.get(position);
            holder.itemNameView.setText("Termék: " + component.getItem());
            holder.amountView.setText("Mennyiség: " + component.getAmount() + " " + component.getUnit());
            holder.compositeItemView.setText("Kompozit termék: " + component.getCompositeItem());
        } else {
            holder.itemNameView.setText("No Item");
        }
    }

    public void setComponents(List<Component> components){
        this.components = components;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (components != null) {
            return components.size();
        }else {
            return 0;
        }
    }

    public Component getComponentAt(int position) {
        return components.get(position);
    }
}
