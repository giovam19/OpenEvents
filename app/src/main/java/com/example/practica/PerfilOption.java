package com.example.practica;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilOption extends AppCompatActivity {
    private ImageView backButton;
    private EditText name;
    private EditText user;
    private EditText email;
    private EditText password;
    private EditText repeatPassword;
    private TextView numCreatedEvents;
    private TextView numParticipatedEvents;
    private TextView numContactedPeople;
    private Button save;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_modificar_perfil);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.light_blue));

        backButton = (ImageView) findViewById(R.id.backButton);
        name = (EditText) findViewById(R.id.userName);
        user = (EditText) findViewById(R.id.user);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        repeatPassword = (EditText) findViewById(R.id.password2);
        numCreatedEvents = (TextView) findViewById(R.id.eventosCreados);
        numParticipatedEvents = (TextView) findViewById(R.id.eventosParticipados);
        numContactedPeople = (TextView) findViewById(R.id.personasContactadas);
        save = (Button) findViewById(R.id.buttonSave);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nam = name.getText().toString();
                String usr = user.getText().toString();
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                String repPass = repeatPassword.getText().toString();
            }
        });
    }
}
