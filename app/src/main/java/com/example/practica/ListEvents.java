package com.example.practica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class ListEvents extends AppCompatActivity {
    private ImageView userImage;
    private TextView userName;
    private Button newTask;
    private Spinner filter;
    private RecyclerView eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);

        userImage = (ImageView) findViewById(R.id.userImage);
        userName = (TextView) findViewById(R.id.userNameText);
        newTask = (Button) findViewById(R.id.newTask);
        filter = (Spinner) findViewById(R.id.filter);
        eventList = (RecyclerView) findViewById(R.id.eventList);


    }
}