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

public class EventActivity extends AppCompatActivity {
    private ImageView backButton;
    private TextView contactButton;
    private TextView eventTitle;
    private Button apuntarse;
    private Button desapuntarse;
    private ImageView imageEvent;
    private TextView monthStart;
    private TextView monthEnd;
    private TextView dayStart;
    private TextView dayEnd;
    private TextView description;
    private TextView ubication;
    private TextView maxPpl;
    private TextView type;

    private int ownerID;
    private int eventID;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_layout);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.light_blue));

        backButton = (ImageView) findViewById(R.id.backButtonEvent);
        contactButton = (TextView) findViewById(R.id.contact);
        eventTitle = (TextView) findViewById(R.id.eventTitle);
        apuntarse = (Button) findViewById(R.id.añadir_event);
        desapuntarse = (Button) findViewById(R.id.quitar_event);
        imageEvent = (ImageView) findViewById(R.id.image_event);
        monthStart = (TextView) findViewById(R.id.monthTextStart);
        monthEnd = (TextView) findViewById(R.id.monthTextEnd);
        dayStart = (TextView) findViewById(R.id.dayTextStart);
        dayEnd = (TextView) findViewById(R.id.dayTextEnd);
        description = (TextView) findViewById(R.id.descriptionText);
        ubication = (TextView) findViewById(R.id.ubicationText);
        maxPpl = (TextView) findViewById(R.id.maxPeopleText);
        type = (TextView) findViewById(R.id.typeText);

        String title = getIntent().getStringExtra("eventTilte");
        String descript = getIntent().getStringExtra("eventDescription");
        String loc = getIntent().getStringExtra("eventUbi");
        String maxppl = getIntent().getStringExtra("eventMaxppl");
        String tpe = getIntent().getStringExtra("eventType");
        ownerID = getIntent().getIntExtra("eventOwner", -1);
        eventID = getIntent().getIntExtra("eventID", -1);

        eventTitle.setText(title);
        description.setText(descript);
        ubication.setText(loc);
        maxPpl.setText(maxppl);
        type.setText(tpe);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: abrir chat del creador
            }
        });

        apuntarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: apuntarse.
            }
        });

        desapuntarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: desapuntarse.
            }
        });
    }
}