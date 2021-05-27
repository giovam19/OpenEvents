package com.example.practica;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class ParticipatedEvents extends AppCompatActivity {
    private ImageView backButton;
    private Spinner filtro;
    private RecyclerView lista;

    private ListEventAdapter adapter;
    private JSONArray eventsToShow;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_personal_events);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.light_blue));

        backButton = (ImageView) findViewById(R.id.backButtonEvent);
        filtro = (Spinner) findViewById(R.id.filterCE);
        lista = (RecyclerView) findViewById(R.id.eventListCE);
        lista.setLayoutManager(new LinearLayoutManager(this));
        eventsToShow = new JSONArray();

        filtro.setVisibility(View.INVISIBLE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getEventsFromAPI();
        //TODO: si es cero agregar algo
        updateUI();
    }

    private void updateUI() {
        adapter = new ListEventAdapter(eventsToShow, null);
        lista.setAdapter(adapter);
    }

    private void getEventsFromAPI() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/users/"+User.getUser().getID()+"/assistances/finished";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                eventsToShow = response;
                updateUI();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("events error: " + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + User.getUser().getToken());
                return params;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID", String.valueOf(User.getUser().getID()));
                return params;
            }
        };

        queue.add(arrayRequest);
    }

}