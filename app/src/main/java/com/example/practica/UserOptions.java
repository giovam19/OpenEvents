package com.example.practica;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserOptions extends AppCompatActivity {
    private ImageView backButton;
    private TextView logout;
    private Button newEvent;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_options);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.light_blue));

        backButton = (ImageView) findViewById(R.id.backButton);
        logout = (TextView) findViewById(R.id.logoutUO);
        newEvent = (Button) findViewById(R.id.newEventUO);

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

        newEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserOptions.this, NewEventActivity.class);
                startActivity(intent);
            }
        });
    }

    private void closeActivity(boolean isYes) {
        Intent data = new Intent();
        data.putExtra("CLOSE_ANSWER", isYes);
        setResult(RESULT_OK, data);
    }
}