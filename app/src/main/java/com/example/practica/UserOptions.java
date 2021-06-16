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

import com.squareup.picasso.Picasso;

public class UserOptions extends AppCompatActivity {
    private ImageView backButton;
    private ImageView userImage;
    private TextView logout;
    private TextView userName;
    private Button newEvent;
    private Button perfil;
    private Button listCreatedEvents;
    private Button listSavedEvents;
    private Button listParticipatedEvents;
    private Button timeline;
    private Button chats;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_options);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.light_blue));

        backButton = (ImageView) findViewById(R.id.backButton);
        userImage = (ImageView) findViewById(R.id.userImageUO);
        logout = (TextView) findViewById(R.id.logoutUO);
        userName = (TextView) findViewById(R.id.userNameUO);
        newEvent = (Button) findViewById(R.id.newEventUO);
        perfil = (Button) findViewById(R.id.perfilButton);
        listCreatedEvents = (Button) findViewById(R.id.createdEvents);
        listSavedEvents = (Button) findViewById(R.id.guardadosUO);
        listParticipatedEvents = (Button) findViewById(R.id.participadosUO);
        chats = (Button) findViewById(R.id.chatsUO);
        timeline = (Button) findViewById(R.id.timelineUO);

        userName.setText(User.getInstance().getUserName());
        if (User.getInstance().getImage().contains("https://") || User.getInstance().getImage().contains("http://"))
            Picasso.get().load(User.getInstance().getImage()).into(userImage);

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

        perfil.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserOptions.this, PerfilOption.class);
                startActivity(intent);
            }
        });

        listCreatedEvents.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserOptions.this, CreatedEventsOption.class);
                startActivity(intent);
            }
        });

        listSavedEvents.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserOptions.this, RegisteredEventsOption.class);
                startActivity(intent);
            }
        });

        listParticipatedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserOptions.this, ParticipatedEvents.class);
                startActivity(intent);
            }
        });

        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserOptions.this, TimelineActivity.class);
                startActivity(intent);
            }
        });

        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserOptions.this, ListChatActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        userName.setText(User.getInstance().getUserName());
    }

    private void closeActivity(boolean isYes) {
        Intent data = new Intent();
        data.putExtra("CLOSE_ANSWER", isYes);
        setResult(RESULT_OK, data);
    }
}