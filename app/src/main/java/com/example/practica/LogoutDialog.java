package com.example.practica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogoutDialog extends AppCompatActivity {
    private Button bYes;
    private Button bNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout_dialog);
        this.setFinishOnTouchOutside(false);

        bYes = (Button) findViewById(R.id.yesButton);
        bNo = (Button) findViewById(R.id.noButton);

        bYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity(true);
                finish();
            }
        });

        bNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity(false);
                finish();
            }
        });
    }

    private void closeActivity(boolean isYes) {
        Intent data = new Intent();
        data.putExtra("CLOSE_ANSWER", isYes);
        setResult(RESULT_OK, data);
    }
}