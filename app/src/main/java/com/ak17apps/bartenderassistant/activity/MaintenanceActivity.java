package com.ak17apps.bartenderassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ak17apps.bartenderassistant.R;

public class MaintenanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        setTitle(getResources().getString(R.string.maintenance));

        Button unitsBtn = findViewById(R.id.unitsBtn);
        unitsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MaintenanceActivity.this, UnitActivity.class));
                finish();
                overridePendingTransition(0, 0);
            }
        });

        Button unitExchangesBtn = findViewById(R.id.unitExchangesBtn);
        unitExchangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MaintenanceActivity.this, UnitExchangeActivity.class));
                finish();
                overridePendingTransition(0, 0);
            }
        });

        Button itemsBtn = findViewById(R.id.itemsBtn);
        itemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MaintenanceActivity.this, ItemActivity.class));
                finish();
                overridePendingTransition(0, 0);
            }
        });

        Button categoriesBtn = findViewById(R.id.categoriesBtn);
        categoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MaintenanceActivity.this, CategoryActivity.class));
                finish();
                overridePendingTransition(0, 0);
            }
        });

        Button sellingAmountsBtn = findViewById(R.id.sellingAmountsBtn);
        sellingAmountsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MaintenanceActivity.this, SellingAmountActivity.class));
                finish();
                overridePendingTransition(0, 0);
            }
        });

        Button compositeItemsBtn = findViewById(R.id.compositeItemsBtn);
        compositeItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MaintenanceActivity.this, CompositeItemActivity.class));
                finish();
                overridePendingTransition(0, 0);
            }
        });

        Button componentsBtn = findViewById(R.id.componentsBtn);
        componentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MaintenanceActivity.this, ComponentActivity.class));
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
        overridePendingTransition(0, 0);
    }
}