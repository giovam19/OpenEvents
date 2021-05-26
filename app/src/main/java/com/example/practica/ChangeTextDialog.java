package com.example.practica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChangeTextDialog extends AppCompatActivity {
    private EditText newText;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_text_dialog);
        setFinishOnTouchOutside(false);

        newText = (EditText) findViewById(R.id.newText);
        save = (Button) findViewById(R.id.saveButton);

        newText.setText(getIntent().getStringExtra("actual"));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("newText", newText.getText().toString());
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }
}