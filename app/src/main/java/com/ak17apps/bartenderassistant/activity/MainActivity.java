package com.ak17apps.bartenderassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ak17apps.bartenderassistant.R;
import com.ak17apps.bartenderassistant.utils.DataHandler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private AppCompatActivity activity = this;
    private int loadCsvRequestCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button boardsBtn = findViewById(R.id.boardsBtn);
        boardsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BoardActivity.class));
                finish();
                overridePendingTransition(0, 0);
            }
        });

        Button maintenanceBtn = findViewById(R.id.maintenanceBtn);
        maintenanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MaintenanceActivity.class));
                finish();
                overridePendingTransition(0, 0);
            }
        });

        Button saveDataBtn = findViewById(R.id.saveDataBtn);
        saveDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHandler.saveData(activity);
            }
        });

        Button loadDataBtn = findViewById(R.id.loadDataBtn);
        loadDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHandler.chooseCSV(activity, loadCsvRequestCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == loadCsvRequestCode && resultCode == RESULT_OK){
            try {
                if(data != null && data.getData() != null) {
                    /*if(!data.getData().getPath().toUpperCase().endsWith("CSV")){
                        Toast.makeText(this, "A kiválasztott fájl nem CSV", Toast.LENGTH_SHORT).show();
                    }else{*/
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        DataHandler.cleanTables(new BufferedReader(new InputStreamReader(inputStream)));
                    //}
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
