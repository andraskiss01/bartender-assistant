package com.ak17apps.bartenderassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ak17apps.bartenderassistant.R;
import com.ak17apps.bartenderassistant.entity.Unit;
import com.ak17apps.bartenderassistant.entity.UnitExchange;
import com.ak17apps.bartenderassistant.viewmodel.UnitExchangeViewModel;

import java.util.List;

public class UnitExchangeListAdapter extends RecyclerView.Adapter<UnitExchangeListAdapter.UnitExchangeViewHolder> {
    private UnitExchangeViewModel unitExchangeViewModel;
    private final LayoutInflater inflater;
    private List<UnitExchange> unitExchanges; // Cached copy of words

    public UnitExchangeListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    class UnitExchangeViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final Button editButton, deleteButton;
        private UnitExchangeViewHolder(final View unitView) {
            super(unitView);
            textViewName = unitView.findViewById(R.id.textView_name);

            editButton = unitView.findViewById(R.id.button_edit);
            deleteButton = unitView.findViewById(R.id.button_delete);
        }

    }

    @Override
    public UnitExchangeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View unitView = inflater.inflate(R.layout.recyclerview_unit_exchange, parent, false);
        return new UnitExchangeViewHolder(unitView);
    }

    @Override
    public void onBindViewHolder(final UnitExchangeViewHolder holder, int position) {
        if (unitExchanges != null) {
            UnitExchange current = unitExchanges.get(position);
            holder.textViewName.setText(current.getFromValue() + " " + current.getFromUnit() + " = " + current.getToValue() + " " + current.getToUnit());
        } else {
            holder.textViewName.setText("No Item");
        }
    }

    public void setUnitExchanges(List<UnitExchange> unitExchanges){
        this.unitExchanges = unitExchanges;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (unitExchanges != null) {
            return unitExchanges.size();
        }else {
            return 0;
        }
    }

    public UnitExchange getUnitExchangeAt(int position) {
        return unitExchanges.get(position);
    }
}
