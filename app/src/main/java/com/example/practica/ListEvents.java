package com.example.practica;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class ListEvents extends AppCompatActivity {
    private ImageView userImage;
    private TextView userName;
    private Button newTask;
    private Spinner filter;
    private RecyclerView eventList;

    private JSONArray eventsArray;
    private ListEventAdapter adapter;

    public static final int FINISH_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);
        getEventsFromAPI();

        userImage = (ImageView) findViewById(R.id.userImage);
        userName = (TextView) findViewById(R.id.userNameText);
        newTask = (Button) findViewById(R.id.newTask);
        filter = (Spinner) findViewById(R.id.filter);
        eventList = (RecyclerView) findViewById(R.id.eventList);

        eventList.setLayoutManager(new LinearLayoutManager(this));
        updateUI();

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListEvents.this, UserOptions.class);
                startActivityForResult(intent, FINISH_CODE);
            }
        });
    }

    private void updateUI() {
        if (adapter == null) {
            adapter = new ListEventAdapter(eventsArray);
            eventList.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void getEventsFromAPI() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://puigmal.salle.url.edu/api/events";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    eventsArray = response;
                    adapter = new ListEventAdapter(eventsArray);
                    eventList.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("onresponse Error " + error);

            }
        });
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ListEvents.this, LogoutDialog.class);
        startActivityForResult(intent, FINISH_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == FINISH_CODE) {
            if (data == null) {
                return;
            }
            boolean endActivity = data.getBooleanExtra("CLOSE_ANSWER", false);

            if (endActivity) {
                finish();
            }
        }
    }
}