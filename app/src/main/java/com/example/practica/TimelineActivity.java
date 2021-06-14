package com.example.practica;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimelineActivity extends AppCompatActivity {
    private ImageView backButton;
    private Spinner filtro;
    private RecyclerView lista;
    private TextView emptyList;
    private TextView text;

    private ListTimelineAdapter adapter;
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
        emptyList = (TextView) findViewById(R.id.emptyListText);
        text = (TextView) findViewById(R.id.textViewEvent);
        lista = (RecyclerView) findViewById(R.id.eventListCE);
        lista.setLayoutManager(new LinearLayoutManager(this));
        eventsToShow = new JSONArray();

        filtro.setVisibility(View.INVISIBLE);
        text.setText("Timeline");
        text.setTextSize(18);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getEventsFromAPI();
        updateUI();
    }

    private void updateUI() {
        adapter = new ListTimelineAdapter(eventsToShow);
        lista.setAdapter(adapter);
    }

    private JSONArray sortByDate(JSONArray entry) {
        JSONArray sorted = new JSONArray();
        List<JSONObject> values = new ArrayList<>();

        for (int i = 0; i < entry.length(); i++) {
            try {
                values.add(entry.getJSONObject(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Collections.sort(values, new Comparator<JSONObject>() {

            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                String mesI = null, diaI = null;
                String mesF = null, diaF = null;

                try {
                    mesI = (String) o1.get("eventStart_date");
                    mesF = (String) o2.get("eventEnd_date");

                    String[] splitDate = mesI.split(":");
                    String[] utilDate = splitDate[0].split("-");
                    String[] day = utilDate[2].split("T");
                    mesI = utilDate[1];
                    diaI = day[0];

                    splitDate = mesF.split(":");
                    utilDate = splitDate[0].split("-");
                    day = utilDate[2].split("T");
                    mesF = utilDate[1];
                    diaF = day[0];

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (mesI.equals(mesF)) {
                    return diaI.compareTo(diaF);
                } else {
                    return mesI.compareTo(mesF); //para cambiar orden -> -valA.compareTo(valB);
                }
            }
        });

        for (int i = 0; i < entry.length(); i++) {
            sorted.put(values.get(i));
        }

        return sorted;
    }

    private void getEventsFromAPI() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/users/"+User.getInstance().getID()+"/assistances/finished";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                eventsToShow = sortByDate(response);
                updateUI();
                if (eventsToShow.length() <= 0) {
                    emptyList.setVisibility(View.VISIBLE);
                }
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
                params.put("Authorization", "Bearer " + User.getInstance().getToken());
                return params;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID", String.valueOf(User.getInstance().getID()));
                return params;
            }
        };

        queue.add(arrayRequest);
    }
}