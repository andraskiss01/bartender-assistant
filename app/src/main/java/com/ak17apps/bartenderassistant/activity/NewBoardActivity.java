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

public class NewBoardActivity extends AppCompatActivity {
    private EditText editBoardNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_board);

        editBoardNameView = findViewById(R.id.edit_board_name);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        Intent intent = getIntent();
        if (intent.hasExtra(ApplicationStrings.BOARD_ID)) {
            setTitle(getResources().getString(R.string.edit_table));
            editBoardNameView.setText(intent.getStringExtra(ApplicationStrings.BOARD_NAME));
        } else {
            setTitle(getResources().getString(R.string.new_table));
        }

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(editBoardNameView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String name = editBoardNameView.getText().toString();
                    replyIntent.putExtra(ApplicationStrings.BOARD_NAME, name);

                    int id = getIntent().getIntExtra(ApplicationStrings.BOARD_ID, -1);
                    if (id != -1) {
                        replyIntent.putExtra(ApplicationStrings.BOARD_ID, id);
                    }

                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
