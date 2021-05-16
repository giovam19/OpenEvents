package com.example.practica;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ListEvents extends AppCompatActivity {
    private ImageView userImage;
    private TextView userName;
    private Button newTask;
    private Spinner filter;
    private RecyclerView eventList;
    private String option;

    private JSONArray eventsArray;
    private JSONArray eventsToShow;
    private ListEventAdapter adapter;

    public static final int FINISH_CODE = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.light_blue));

        userImage = (ImageView) findViewById(R.id.userImage);
        userName = (TextView) findViewById(R.id.userNameText);
        newTask = (Button) findViewById(R.id.newTask);
        filter = (Spinner) findViewById(R.id.filter);
        eventList = (RecyclerView) findViewById(R.id.eventList);
        eventList.setLayoutManager(new LinearLayoutManager(this));
        eventsToShow = new JSONArray();

        userName.setText(User.getUser().getUserName());
        getEventsFromAPI();

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this, R.array.filter_modes, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter.setAdapter(adapterSpinner);
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                option = parent.getItemAtPosition(position).toString();
                if (!option.equals("All Events")) {
                    eventsToShow = fillArray(option);
                } else {
                    eventsToShow = eventsArray;
                }
                updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListEvents.this, UserOptions.class);
                startActivityForResult(intent, FINISH_CODE);
            }
        });

        newTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListEvents.this, NewEventActivity.class);
                startActivity(intent);
                updateUI();
            }
        });

        updateUI();
    }

    private void updateUI() {
        adapter = new ListEventAdapter(eventsToShow);
        eventList.setAdapter(adapter);
    }

    private JSONArray fillArray(String option) {
        try {
            JSONArray array = new JSONArray();

            for (int i = 0; i < eventsArray.length(); i++) {
                String op = (String) eventsArray.getJSONObject(i).get("type");
                if (op.equals(option)) {
                    array.put(eventsArray.getJSONObject(i));
                }
            }

            return array;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void getEventsFromAPI() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/events";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    eventsArray = response;
                    eventsToShow = eventsArray;
                    adapter = new ListEventAdapter(eventsToShow);
                    eventList.setAdapter(adapter);
                    updateUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("onresponse Error " + error);

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer" + User.getUser().getToken());
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ListEvents.this, LogoutDialog.class);
        startActivityForResult(intent, FINISH_CODE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        userName.setText(User.getUser().getUserName());
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