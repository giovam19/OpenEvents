package com.example.practica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UserOptions extends AppCompatActivity {
    private ImageView backButton;
    private TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_options);

        backButton = (ImageView) findViewById(R.id.backButton);
        logout = (TextView) findViewById(R.id.logoutUO);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity(false);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity(true);
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