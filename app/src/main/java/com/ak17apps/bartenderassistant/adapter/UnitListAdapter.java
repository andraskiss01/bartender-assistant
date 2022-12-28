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
import com.ak17apps.bartenderassistant.viewmodel.UnitViewModel;

import java.util.List;

public class UnitListAdapter extends RecyclerView.Adapter<UnitListAdapter.UnitViewHolder> {
private UnitViewModel unitViewModel;

    class UnitViewHolder extends RecyclerView.ViewHolder {
        private final TextView unitNameView;
        private final Button editButton, deleteButton;
        private UnitViewHolder(final View unitView) {
            super(unitView);
            unitNameView = unitView.findViewById(R.id.textView_name);

            editButton = unitView.findViewById(R.id.button_edit);
            deleteButton = unitView.findViewById(R.id.button_delete);
        }
    }

    private final LayoutInflater inflater;
    private List<Unit> units; // Cached copy of words

    public UnitListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    @Override
    public UnitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View unitView = inflater.inflate(R.layout.recyclerview_unit, parent, false);
        return new UnitViewHolder(unitView);
    }

    @Override
    public void onBindViewHolder(final UnitViewHolder holder, int position) {
        if (units != null) {
            Unit current = units.get(position);
            holder.unitNameView.setText("Név: " + current.getName());
        } else {
            holder.unitNameView.setText("No Item");
        }
    }

    public void setUnits(List<Unit> units){
        this.units = units;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (units != null) {
            return units.size();
        }else {
            return 0;
        }
    }

    public Unit getUnitAt(int position) {
        return units.get(position);
    }
}
