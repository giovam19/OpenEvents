package com.example.practica;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class CreatedEventsOption extends AppCompatActivity implements FilterOptionListener {
    private static final String URL_FUTURE = "http://puigmal.salle.url.edu/api/users/"+User.getInstance().getID()+"/events/future";
    private static final String URL_FINISHED = "http://puigmal.salle.url.edu/api/users/"+User.getInstance().getID()+"/events/finished";
    private static final String URL_CURRENT = "http://puigmal.salle.url.edu/api/users/"+User.getInstance().getID()+"/events/current";
    public static final String FINISHED = "Finished";
    public static final String FUTURE = "Future";
    public static final String CURRENT = "Current";

    private ImageView backButton;
    private Spinner filter;
    private RecyclerView lista;
    private String option;
    private TextView emptyList;

    private JSONArray eventsFinished;
    private JSONArray eventsFuture;
    private JSONArray eventsCurrent;
    private JSONArray eventsToShow;
    private ListEventAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_personal_events);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.light_blue));

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this, R.array.filter_created, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        backButton = (ImageView) findViewById(R.id.backButtonEvent);
        filter = (Spinner) findViewById(R.id.filterCE);
        emptyList = (TextView) findViewById(R.id.emptyListText);
        lista = (RecyclerView) findViewById(R.id.eventListCE);
        lista.setLayoutManager(new LinearLayoutManager(this));
        eventsToShow = new JSONArray();
        eventsFinished = new JSONArray();
        eventsFuture = new JSONArray();
        eventsCurrent = new JSONArray();

        filter.setAdapter(adapterSpinner);
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                option = parent.getItemAtPosition(position).toString();
                eventsToShow = fillArray(option);
                updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getEventsFromAPI(URL_FINISHED, FINISHED);
        getEventsFromAPI(URL_CURRENT, CURRENT);
        getEventsFromAPI(URL_FUTURE, FUTURE);
    }

    private void updateUI() {
        adapter = new ListEventAdapter(eventsToShow, this);
        lista.setAdapter(adapter);
    }

    private JSONArray fillArray(String option) {
        JSONArray array = new JSONArray();

        switch (option) {
            case FINISHED:
                array = eventsFinished;
                break;
            case FUTURE:
                array = eventsFuture;
                break;
            case CURRENT:
                array = eventsCurrent;
                break;
        }

        if (array.length() <= 0) {
            String message = "There is no created "+ option +" event";
            emptyList.setVisibility(View.VISIBLE);
            emptyList.setText(message);
        } else {
            emptyList.setVisibility(View.INVISIBLE);
        }

        return array;
    }

    private void getEventsFromAPI(String url, String choice) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                switch (choice) {
                    case FINISHED:
                        eventsFinished = response;
                        break;
                    case FUTURE:
                        eventsFuture = response;
                        if (eventsFuture.length() <= 0) {
                            emptyList.setVisibility(View.VISIBLE);
                            emptyList.setText("There is no created Future event");
                        } else {
                            emptyList.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case CURRENT:
                        eventsCurrent = response;
                        break;

                }
                eventsToShow = eventsFuture;
                updateUI();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("onresponse Error " + error);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ID", String.valueOf(User.getInstance().getID()));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + User.getInstance().getToken());
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }

    @Override
    public String getOptionSelected() {
        return option;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getEventsFromAPI(URL_FUTURE, FUTURE);
        updateUI();
    }
}
