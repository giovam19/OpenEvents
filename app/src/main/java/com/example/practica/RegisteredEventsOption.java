package com.example.practica;

import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisteredEventsOption extends AppCompatActivity {
    private ImageView backButton;
    private Spinner filtro;
    private RecyclerView lista;
    private TextView emptyList;

    private ListEventAdapter adapter;
    private JSONArray eventsToShow;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_personal_events);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.light_blue));

        backButton = (ImageView) findViewById(R.id.backButtonEvent);
        filtro = (Spinner) findViewById(R.id.filterCE);
        lista = (RecyclerView) findViewById(R.id.eventListCE);
        emptyList = (TextView) findViewById(R.id.emptyListText);
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
        updateUI();

        registerForContextMenu(lista);
    }

    private void updateUI() {
        adapter = new ListEventAdapter(eventsToShow, null);
        lista.setAdapter(adapter);
    }

    private void getEventsFromAPI() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/users/"+User.getInstance().getID()+"/assistances/future";

        JsonArrayRequest or = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {
                eventsToShow = response;
                updateUI();

                if (eventsToShow == null || eventsToShow.length() <= 0) {
                    emptyList.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error: "+error);
            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ID", String.valueOf(User.getInstance().getID()));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + User.getInstance().getToken());
                return params;
            }
        };

        queue.add(or);
    }

    private void makeNonAssistances(int eventID) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/events/"+eventID+"/assistances";

        JsonObjectRequest or = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                Toast toast = Toast.makeText(RegisteredEventsOption.this, "Confirmed Non Assistance", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 40);
                toast.show();

                recreate();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(RegisteredEventsOption.this, "Error making the request", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 40);
                toast.show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + User.getInstance().getToken());
                return params;
            }
        };

        queue.add(or);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_desapuntar, menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.desapuntarse) {
            makeNonAssistances(adapter.getEventID());
            return true;
        } else {
            return super.onContextItemSelected(item);

        }
    }
}
