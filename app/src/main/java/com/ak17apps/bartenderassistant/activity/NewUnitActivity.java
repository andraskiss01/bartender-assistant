package com.ak17apps.bartenderassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.ak17apps.bartenderassistant.R;
import com.ak17apps.bartenderassistant.utils.ApplicationStrings;

public class NewUnitActivity extends AppCompatActivity {
    private EditText editUnitNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_unit);

        editUnitNameView = findViewById(R.id.edit_unit_name);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        Intent intent = getIntent();
        if (intent.hasExtra(ApplicationStrings.UNIT_ID)) {
            setTitle(getResources().getString(R.string.edit_unit));
            editUnitNameView.setText(intent.getStringExtra(ApplicationStrings.UNIT_NAME));
        } else {
            setTitle(getResources().getString(R.string.new_unit));
        }

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(editUnitNameView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String productName = editUnitNameView.getText().toString();

                    replyIntent.putExtra(ApplicationStrings.UNIT_NAME, productName);

                    int id = getIntent().getIntExtra(ApplicationStrings.UNIT_ID, -1);
                    if (id != -1) {
                        replyIntent.putExtra(ApplicationStrings.UNIT_ID, id);
                    }

                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
